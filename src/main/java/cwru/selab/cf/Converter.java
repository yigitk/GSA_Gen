package cwru.selab.cf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Converter extends Java8BaseListener {

	final int IDENTIFIER_TYPE = 102;

	Java8Parser parser;

	// Rewriting mechanism
	TokenStreamRewriter rewriter;

	// Tokens from the program
	TokenStream tokens;

	// Variables in predicate block being assigned
	Stack<HashSet<String>> predicateBlockVariablesStack = new Stack<>();

	// Variable subscripts before entering the predicate block
	Stack<HashMap<String, Integer>> varSubscriptsBeforePredicateStack = new Stack<>();

	// Keep track of variables in scope
	HashMap<String, Integer> variableSubscripts = new HashMap<>();

	// Keep track of variables that were declared in a predicate block
	Stack<HashSet<String>> variablesDeclaredInPredicateStack = new Stack<>();

	// All variables used in the program
	HashSet<String> allLocalVariables = new HashSet<>();

	// All variables initialized in the current scope
	Stack<HashSet<String>> variablesInitializedInScope = new Stack<>();

	// Map of variables and their confounding adjustment variables
	HashMap<String, HashSet<String>> causalMap = new HashMap<>();

	int scope = 0;
	boolean alreadyInClass=false;
	boolean notMethod=false;
	String packageName = "";
	String className = "";
	String currentMethodName = "";

	public Converter(Java8Parser parser, TokenStreamRewriter rewriter) {
		this.rewriter = rewriter;
		this.parser = parser;
		this.tokens = parser.getTokenStream();
	}

	/**
	 * Get causal map
	 */
	public HashMap<String, HashSet<String>> getCausalMap() {
		return causalMap;
	}

	/**
	 * Get package name
	 */
	@Override
	public void enterPackageName(Java8Parser.PackageNameContext ctx) {
		packageName = ctx.getText();
	}

	/**
	 * Increment scope tracker and track variables initialized in this scope
	 */
	@Override
	public void enterBlock(Java8Parser.BlockContext ctx) {
		scope++;
		variablesInitializedInScope.push(new HashSet<String>());
	}

	/**
	 * Decrement scope tracker and view variables initilialized in previous scope
	 */
	public void exitBlock(Java8Parser.BlockContext ctx) {
		scope--;
		variablesInitializedInScope.pop();
	}

	/**
	 * Get variable on the left hand side of an assignment, increment subscript
	 */
	@Override
	public void enterLeftHandSide(Java8Parser.LeftHandSideContext ctx) {
		if (ctx.expressionName() == null)
			return;
		String variable = tokens.getText(ctx);

		if (insidePredicateBlock(ctx)) {
			for (HashSet<String> predicateBlockVariables : predicateBlockVariablesStack) {
				predicateBlockVariables.add(variable);
			}
		}
	}

	/**
	 * Handle new variable declarations by getting variable name, checking if a variable of the same
	 * name has already been declared for subscripting purposes. Insert a recording statement and a 
	 * subscript incrementation
	 * @param ctx
	 */
	@Override
	public void enterLocalVariableDeclaration(Java8Parser.LocalVariableDeclarationContext ctx) {
		String type = tokens.getText(ctx.unannType());
		if (ctx.unannType().unannReferenceType() == null)
			for (int i = 0; i < ctx.variableDeclaratorList().variableDeclarator().size(); i++) {
				Java8Parser.VariableDeclaratorIdContext varContext = ctx.variableDeclaratorList().variableDeclarator(i)
					.variableDeclaratorId();
				if (varContext.dims() != null)
					continue;

				String variable = tokens.getText(varContext);

				if (!variableSubscripts.containsKey(variable))
					variableSubscripts.put(variable, -1);
				int lineNumber = ctx.getStart().getLine();
				if (insidePredicateBlock(ctx)) {
					for (HashSet<String> variablesDeclaredInPredicate : variablesDeclaredInPredicateStack) {
						variablesDeclaredInPredicate.add(variable);
					}
				}

				if (!isDescendantOf(ctx, Java8Parser.ForInitContext.class)
						&& ctx.variableDeclaratorList().variableDeclarator(i).variableInitializer() != null) {
					ArrayList<String> expressionNames = getAllExpressionNamesFromPredicate(
							ctx.variableDeclaratorList().variableDeclarator(i).variableInitializer());
					ArrayList<String> expressionNamesSSA = new ArrayList<>();
					for (String expressionName : expressionNames) {
						if (variableSubscripts.containsKey(expressionName))
							expressionNamesSSA.add(expressionName + "_" + variableSubscripts.get(expressionName));
					}

					variablesInitializedInScope.lastElement().add(variable);

					insertVersionUpdateAfter(ctx.getParent().getParent().getStop(), variable);
					insertRecordStatementAfter(ctx.getParent().getStop(), variable, lineNumber);

					if (variableSubscripts.containsKey(variable)) {
						causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
						causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(expressionNamesSSA);
					}

					HashSet<String> postFixAlteredVariables = getIncrementAndDecrementVariablesFromAssignment(ctx);
					for (String alteredVariable : postFixAlteredVariables) {
						if (insidePredicateBlock(ctx)) {
							for (HashSet<String> predicateBlockVariables : predicateBlockVariablesStack) {
								predicateBlockVariables.add(alteredVariable);
							}
						}
					}
						}
				allLocalVariables.add(variable);

			}

	}

	/**
	 * 
	 * @param ctx
	 */
	@Override
	public void enterExpressionName(Java8Parser.ExpressionNameContext ctx) {

		// Only changing right hand side in this context
		if (isDescendantOf(ctx, Java8Parser.LeftHandSideContext.class)) {
			return;
		}

		String varName = tokens.getText(ctx);
	}

	// Upon exiting an assignment, increment the subscript counter
	@Override
	public void exitAssignment(Java8Parser.AssignmentContext ctx) {
		String variable = tokens.getText(ctx.leftHandSide());
		int subscript = 0;
		variablesInitializedInScope.lastElement().add(variable);
		if (ctx.leftHandSide().expressionName() == null)
			return;

		ArrayList<String> expressionNames = getAllExpressionNamesFromPredicate(ctx.expression());
		ArrayList<String> expressionNamesSSA = new ArrayList<>();
		for (String expressionName : expressionNames) {
			if (!variableSubscripts.keySet().isEmpty())
				if (variableSubscripts.containsKey(expressionName))
					expressionNamesSSA.add(expressionName + "_" + variableSubscripts.get(expressionName));
		}

		int lineNumber = ctx.getStart().getLine();

		if (!isDescendantOf(ctx, Java8Parser.ForInitContext.class)
				&& !isDescendantOf(ctx, Java8Parser.ForUpdateContext.class)) {
			ParserRuleContext currentContext = ctx;

			while (currentContext.getParent() != null) {
				if (currentContext instanceof Java8Parser.ExpressionStatementContext) {
					insertVersionUpdateAfter(currentContext.getStop(), variable);
					insertRecordStatementAfter(currentContext.getStop(), variable, lineNumber);
					break;
				}
				currentContext = currentContext.getParent();
			}

			if (variableSubscripts.containsKey(variable)) {
				causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
				causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(expressionNamesSSA);
			}
				}
		//    HashSet<String> postFixAlteredVariables = getIncrementAndDecrementVariablesFromAssignment(ctx);
		//    for (String alteredVariable : postFixAlteredVariables) {
		//      insertVersionUpdateAfter(ctx.getParent().getParent().getStop(), alteredVariable);
		//      insertRecordStatementAfter(ctx.getParent().getParent().getStop(), alteredVariable, lineNumber);
		//      if (insidePredicateBlock(ctx)) {
		//        for (HashSet<String> predicateBlockVariables : predicateBlockVariablesStack) {
		//          predicateBlockVariables.add(alteredVariable);
		//        }
		//      }
		//    }

	}

	ArrayList<String> methodParameters = new ArrayList<>();

	// Get the parameters from the method and add them to the maps
	@Override
	public void enterFormalParameter(Java8Parser.FormalParameterContext ctx) {

		if (ctx.variableDeclaratorId().dims() != null)
			return;
		String varName = tokens.getText(ctx.variableDeclaratorId());
		String varType = tokens.getText(ctx.unannType());
		if("String".equals(varType) || "int".equals(varType) || "double".equals(varType) || "short".equals(varType) || "long".equals(varType)
				|| "char".equals(varType) || "float".equals(varType) ||"boolean".equals(varType)) {
			methodParameters.add(varName);
			if (!variableSubscripts.containsKey(varName))
				variableSubscripts.put(varName, -1);
				}
	}

	/**
	 * Get the name of the method
	 * @param ctx
	 */
	@Override
	public void enterMethodDeclarator(Java8Parser.MethodDeclaratorContext ctx) {

		String methodName = ctx.getChild(0).getText();
		currentMethodName = methodName;

	}

	/**
	 * Get the name of the class, track a new level of scope 
	 * @param ctx
	 */
	@Override
	public void enterNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {

		int classTokenNumber = -1;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i).getText().equals("class"))
				classTokenNumber = i;
		}
		String className = ctx.getChild(classTokenNumber + 1).getText();
		this.className = className;
		// TerminalNode t = (TerminalNode) ctx.getChild(2);
		variablesInitializedInScope.push(new HashSet<String>());
		// rewriter.replace(t.getSymbol(), className + "Fault");
	}

	@Override
	public void exitNormalClassDeclaration(Java8Parser.NormalClassDeclarationContext ctx) {
		variablesInitializedInScope.pop();
	}

	public void enterCompilationUnit(Java8Parser.CompilationUnitContext ctx) {
		if (ctx.packageDeclaration() != null)
			rewriter.insertAfter(ctx.packageDeclaration().getStop(),
					"import java.io.BufferedWriter;import java.io.FileWriter;import java.io.IOException;import java.util.HashMap;import AntlrExperiments.CollectOut;");
		else {
			rewriter.insertBefore(ctx.getStart(),
					"import java.io.BufferedWriter;import java.io.FileWriter;import java.io.IOException;import java.util.HashMap;import AntlrExperiments.CollectOut;");
		}
	}

	/*@Override
	public void enterClassBody(Java8Parser.ClassBodyContext ctx) {
		// rewriter.insertAfter(ctx.getStart(),
		//     "public static void record(String packageName, String clazz, String method, int line, int staticScope,String variableName, Object value, int version) {BufferedWriter writer = null;try {writer = new BufferedWriter(new FileWriter(\"output.txt\", true));} catch (IOException e) {System.out.println(e.getMessage());}try {writer.append(clazz + \",\" + method + \",\" + line + \",\" + staticScope + \",\" + variableName + \",\"+ version + \",\" + value + \"\\n\");writer.close();} catch (Exception e) {System.out.println(e.getMessage());}}");
		if(!alreadyInClass){

			rewriter.insertAfter(ctx.getStart(),
					"static HashMap<String, String> __versionMap__ = new HashMap<String, String>(); public static void record(String packageName, String clazz, String method, int line, int staticScope,String variableName, Object value, int version) {__versionMap__.putIfAbsent(variableName + \"_\" + version, clazz + \",\" + method + \",\" + line + \",\" + staticScope + \",\"+ variableName + \",\" + version + \",\" + value + \"\\n\");__versionMap__.put(variableName + \"_\" + version, clazz + \",\" + method + \",\" + line + \",\" + staticScope + \",\"+ variableName + \",\" + version + \",\" + value + \"\\n\");}public static void writeOutVariables() {BufferedWriter writer = null;try {writer = new BufferedWriter(new FileWriter(\"output.txt\", true));} catch (IOException e) {System.out.println(e.getMessage());}try {for (String variableVersion : __versionMap__.keySet()) {writer.append(__versionMap__.get(variableVersion));}writer.close();} catch (Exception e) {System.out.println(e.getMessage());}}");
			alreadyInClass=true;
		}
		// rewriter.insertAfter(ctx.getStart(),
		//     "public int fluky(int correctValue, double probability) {if (Math.random() < probability)return (int) (correctValue * 2 * Math.random());else return correctValue;}public double fluky(double correctValue, double probability) {if (Math.random() < probability)return (correctValue * 2 * Math.random());else return correctValue;}public long fluky(long correctValue, double probability) {if (Math.random() < probability)return (long) (correctValue * 2 * Math.random());else return correctValue;}      public short fluky(short correctValue, double probability) {if (Math.random() < probability)return (short) (correctValue * 2 * Math.random());else return correctValue;}");
	}
*/
	HashMap<String, Integer> subscriptsBeforeMethod = new HashMap<>();

	HashSet<String> variablesTrackedInMethod = new HashSet<String>();

	HashMap<String, Integer> initializeFormalParams = new HashMap<>();

	boolean alreadyInMethod=false;

	@Override
	public void enterMethodBody(Java8Parser.MethodBodyContext ctx) {
		if (ctx.getChild(0).getText().equals(";")) {
			notMethod=true;
		}
		if(!notMethod){
			if(alreadyInMethod) {

				methodParameters.clear();
				initializeFormalParams.clear();
			}

			alreadyInMethod=true;
			subscriptsBeforeMethod.putAll(variableSubscripts);

			for (String variable : methodParameters) {
				// System.out.println(variable + " " + subscriptsBeforeMethod.get(variable));
				// System.out.println(variable + " " + variableSubscripts.get(variable));
				if (variableSubscripts.containsKey(variable)) {
					variableSubscripts.put(variable, variableSubscripts.get(variable) + 1);
					initializeFormalParams.put(variable, variableSubscripts.get(variable));
				} else
					initializeFormalParams.put(variable, 0);
				variablesTrackedInMethod.add(variable);
			}
		}
	}

	// When exiting a method, initilize all variables (both from parameters and in body)

	@Override
	public void exitMethodBody(Java8Parser.MethodBodyContext ctx) {
		if(!notMethod){
			for (String variable : allLocalVariables) {
				if (!methodParameters.contains(variable)) {
					/*if (subscriptsBeforeMethod.containsKey(variable)) {
						// System.out.println(variable + " " + subscriptsBeforeMethod.get(variable));
						rewriter.insertAfter(ctx.getStart(),
								"int " + variable + "_version"+subscriptsBeforeMethod.get(variable) + " = " + subscriptsBeforeMethod.get(variable) + ";");
					} */
					variablesTrackedInMethod.add(variable);
				}
			}
			for (String variable : initializeFormalParams.keySet()) {
				//rewriter.insertAfter(ctx.getStart(),"int " + variable + "_version"+initializeFormalParams.get(variable) + " = " + initializeFormalParams.get(variable) + ";");
				insertRecordStatementAfter(ctx.getStart(), variable, ctx.getStart().getLine(),initializeFormalParams.get(variable));
			}

			currentMethodName = "";
			initializeFormalParams.clear();
			allLocalVariables.clear();
			methodParameters.clear();
			variablesTrackedInMethod.clear();
			//System.out.println(variableSubscripts);
			notMethod=false;
			alreadyInMethod=false;
		}
		currentMethodName = "";
		initializeFormalParams.clear();
		allLocalVariables.clear();
		methodParameters.clear();
		variablesTrackedInMethod.clear();
	}

	@Override
	public void enterConstructorBody(Java8Parser.ConstructorBodyContext ctx) {

		//subscriptsBeforeMethod.putAll(variableSubscripts);
		for (String variable : methodParameters) {
			if (variableSubscripts.containsKey(variable)) {
				variableSubscripts.put(variable, variableSubscripts.get(variable) + 1);
				initializeFormalParams.put(variable, variableSubscripts.get(variable));
			} else
				initializeFormalParams.put(variable, 0);
		}
	}

	@Override
	public void exitConstructorBody(Java8Parser.ConstructorBodyContext ctx) {
		if (ctx.explicitConstructorInvocation() != null) {
			/*for (String variable : allLocalVariables) {
				if (!methodParameters.contains(variable)) {
					if (subscriptsBeforeMethod.containsKey(variable))
						rewriter.insertAfter(ctx.explicitConstructorInvocation().getStop(),
								"int " + variable + "_version"+subscriptsBeforeMethod.get(variable) + " = " + subscriptsBeforeMethod.get(variable) + ";");

				}
			}*/
			for (String variable : initializeFormalParams.keySet()) {
				//rewriter.insertAfter(ctx.explicitConstructorInvocation().getStop(),"int " + variable + "_version"+initializeFormalParams.get(variable) + " = " + initializeFormalParams.get(variable) + ";");
				insertRecordStatementAfter(ctx.explicitConstructorInvocation().getStop(), variable, ctx.getStart().getLine(),initializeFormalParams.get(variable));
			}
		} else {
			/*for (String variable : allLocalVariables) {
				if (!methodParameters.contains(variable)) {
					if (subscriptsBeforeMethod.containsKey(variable))
						rewriter.insertAfter(ctx.getStart(),
								"int " + variable + "_version"+subscriptsBeforeMethod.get(variable) + " = " + subscriptsBeforeMethod.get(variable) + ";");

				}
			}*/
			for (String variable : initializeFormalParams.keySet()) {
				//rewriter.insertAfter(ctx.getStart(),"int " + variable + "_version"+initializeFormalParams.get(variable) + " = " + initializeFormalParams.get(variable) + ";");
				insertRecordStatementAfter(ctx.getStart(), variable, ctx.getStart().getLine(),initializeFormalParams.get(variable));
			}
		}
		currentMethodName = "";
		initializeFormalParams.clear();
		allLocalVariables.clear();
		methodParameters.clear();
		variablesTrackedInMethod.clear();
	}

	// Replace ++ with intitializaton of a new variable
	@Override
	public void exitPostIncrementExpression(Java8Parser.PostIncrementExpressionContext ctx) {

		if (isDescendantOf(ctx, Java8Parser.ForUpdateContext.class) || ctx.postfixExpression().expressionName() == null)
			return;

		String varName = tokens.getText(ctx.postfixExpression().expressionName());

		ParserRuleContext currentContext = ctx;

		ArrayList<String> confounders = new ArrayList<>();
		if (variableSubscripts.containsKey(varName))
			confounders.add(varName + "_" + variableSubscripts.get(varName));

		while (currentContext.getParent() != null) {
			if (currentContext instanceof Java8Parser.ExpressionStatementContext) {
				insertVersionUpdateAfter(currentContext.getStop(), varName);
				insertRecordStatementAfter(currentContext.getStop(), varName, currentContext.getStart().getLine());
				break;
			}
			currentContext = currentContext.getParent();
		}
		if (variableSubscripts.containsKey(varName)) {
			causalMap.put(varName + "_" + variableSubscripts.get(varName), new HashSet<String>());
			causalMap.get(varName + "_" + variableSubscripts.get(varName)).addAll(confounders);
		}
	}

	// Replace -- with initialization of a new variable
	@Override
	public void exitPostDecrementExpression(Java8Parser.PostDecrementExpressionContext ctx) {
		String varName = tokens.getText(ctx.postfixExpression().expressionName());
		if (isDescendantOf(ctx, Java8Parser.ForUpdateContext.class))
			return;

		ParserRuleContext currentContext = ctx;

		ArrayList<String> confounders = new ArrayList<>();
		if (variableSubscripts.containsKey(varName))
			confounders.add(varName + "_" + variableSubscripts.get(varName));
		while (currentContext.getParent() != null) {
			if (currentContext instanceof Java8Parser.ExpressionStatementContext) {
				insertVersionUpdateAfter(currentContext.getStop(), varName);
				insertRecordStatementAfter(currentContext.getStop(), varName, currentContext.getStart().getLine());
				break;
			}
			currentContext = currentContext.getParent();
		}
		if (variableSubscripts.containsKey(varName)) {
			causalMap.put(varName + "_" + variableSubscripts.get(varName), new HashSet<String>());
			causalMap.get(varName + "_" + variableSubscripts.get(varName)).addAll(confounders);
		}
	}

	@Override
	public void enterIfThenStatement(Java8Parser.IfThenStatementContext ctx) {
		updateVariableSubscriptPredicateStack();
		variablesDeclaredInPredicateStack.push(new HashSet<String>());
		predicateBlockVariablesStack.push(new HashSet<String>());
	}

	@Override
	public void exitIfThenStatement(Java8Parser.IfThenStatementContext ctx) {
		HashMap<String, Integer> varSubscriptsBeforePredicate = varSubscriptsBeforePredicateStack.pop();
		HashSet<String> variablesDeclaredInPredicate = variablesDeclaredInPredicateStack.pop();
		String type = "Integer";
		ParserRuleContext exprCtx = ctx.expression();

		//    rewriter.insertBefore(ctx.statement().getStart(), "{");
		//    rewriter.insertAfter(ctx.statement().getStop(), "}");
		for (String var : predicateBlockVariablesStack.pop()) {
			if (!variableSubscripts.containsKey(var) || variablesDeclaredInPredicate.contains(var)
					|| !variablesInitializedInScope.lastElement().contains(var))
				continue;
			ArrayList<String> confounders = new ArrayList<>();
			confounders.add(var + "_" + varSubscriptsBeforePredicate.get(var));
			confounders.add(var + "_" + variableSubscripts.get(var));

			insertVersionUpdateAfter(ctx.statement().getStart(), var);
			insertRecordStatementAfter(ctx.statement().getStart(), var, ctx.getStop().getLine());
			if (variableSubscripts.containsKey(var)) {
				causalMap.put(var + "_" + variableSubscripts.get(var), new HashSet<String>());
				causalMap.get(var + "_" + variableSubscripts.get(var)).addAll(confounders);
			}
		}

	}

	Stack<MutablePair<HashMap<String, Integer>, HashMap<String, Integer>>> ifThenElseBranchStack = new Stack<>();

	@Override
	public void enterIfThenElseStatement(Java8Parser.IfThenElseStatementContext ctx) {
		updateVariableSubscriptPredicateStack();
		predicateBlockVariablesStack.push(new HashSet<String>());
		variablesDeclaredInPredicateStack.push(new HashSet<String>());
		ifThenElseBranchStack.push(new MutablePair<HashMap<String, Integer>, HashMap<String, Integer>>());
	}

	@Override
	public void exitIfThenElseStatement(Java8Parser.IfThenElseStatementContext ctx) {
		HashSet<String> variablesDeclaredInPredicate = variablesDeclaredInPredicateStack.pop();
		// System.out.println(ctx.getStart().getLine());
		HashMap<String, Integer> varSubscriptsBeforePredicate = varSubscriptsBeforePredicateStack.pop();
		String type = "Integer";
		ParserRuleContext exprCtx = ctx.expression();
		MutablePair<HashMap<String, Integer>, HashMap<String, Integer>> ifThenElseBranchedVariables = ifThenElseBranchStack
			.pop();
		HashMap<String, Integer> ifBranchVariableSubscripts = ifThenElseBranchedVariables.left;
		HashMap<String, Integer> elseBranchVariableSubscripts = ifThenElseBranchedVariables.right;

		//    rewriter.insertBefore(ctx.statementNoShortIf().getStart(), "{");
		//    rewriter.insertAfter(ctx.statementNoShortIf().getStop(), "}");
		HashSet<String> ifBlockVariables = getAllAlteredVariablesFromContext(ctx.statementNoShortIf());
		//
		//    rewriter.insertBefore(ctx.statement().getStart(), "{");
		//    rewriter.insertAfter(ctx.statement().getStop(), "}");
		HashSet<String> elseBlockVariables = getAllAlteredVariablesFromContext(ctx.statement());

		HashSet<String> predicateBlockVariables = predicateBlockVariablesStack.pop();
		for (String var : predicateBlockVariables) {
			if (!variableSubscripts.containsKey(var) || variablesDeclaredInPredicate.contains(var)
					|| !variablesInitializedInScope.lastElement().contains(var))
				continue;
			//Changed
			insertVersionUpdateAfter(ctx.statementNoShortIf().getStart(), var);
			insertRecordStatementAfter(ctx.statementNoShortIf().getStart(), var, ctx.getStop().getLine());
			//      insertVersionUpdateAfter(ctx.statement().getStart(), var);
			//      insertRecordStatementAfter(ctx.statement().getStart(), var, ctx.getStop().getLine());
		}

		HashSet<String> allAddedVariables = new HashSet<>();
		for (String var : ifBlockVariables) {
			if (!variableSubscripts.containsKey(var) || variablesDeclaredInPredicate.contains(var)
					|| !variablesInitializedInScope.lastElement().contains(var))
				continue;
			if (variableSubscripts.containsKey(var)) {
				causalMap.put(var + "_" + variableSubscripts.get(var), new HashSet<String>());
				causalMap.get(var + "_" + variableSubscripts.get(var)).add(var + "_" + ifBranchVariableSubscripts.get(var));
			}

			allAddedVariables.add(var);
		}

		for (String var : elseBlockVariables) {
			if (!variableSubscripts.containsKey(var) || variablesDeclaredInPredicate.contains(var)
					|| !variablesInitializedInScope.lastElement().contains(var))
				continue;
			causalMap.put(var + "_" + variableSubscripts.get(var), new HashSet<String>());
			causalMap.get(var + "_" + variableSubscripts.get(var)).add(var + "_" + elseBranchVariableSubscripts.get(var));

			allAddedVariables.add(var);
		}

		for (String var : allAddedVariables) {
			if (!variableSubscripts.containsKey(var) || variablesDeclaredInPredicate.contains(var)
					|| !variablesInitializedInScope.lastElement().contains(var))
				continue;
			if (causalMap.get(var + "_" + variableSubscripts.get(var)).size() == 1) {
				causalMap.get(var + "_" + variableSubscripts.get(var)).add(var + "_" + varSubscriptsBeforePredicate.get(var));
			}
		}

	}

	@Override
	public void enterIfThenElseStatementNoShortIf(Java8Parser.IfThenElseStatementNoShortIfContext ctx) {
		variablesDeclaredInPredicateStack.push(new HashSet<String>());
		// System.out.println(ctx.getStart().getLine());
		updateVariableSubscriptPredicateStack();
		predicateBlockVariablesStack.push(new HashSet<String>());
		ifThenElseBranchStack.push(new MutablePair<HashMap<String, Integer>, HashMap<String, Integer>>());
	}

	@Override
	public void exitIfThenElseStatementNoShortIf(Java8Parser.IfThenElseStatementNoShortIfContext ctx) {
		// Get all variables initialized inside the if/else block
		HashSet<String> variablesDeclaredInPredicate = variablesDeclaredInPredicateStack.pop();

		// Get the variable versions that existed before entering the if/else block
		HashMap<String, Integer> varSubscriptsBeforePredicate = varSubscriptsBeforePredicateStack.pop();

		// Get the subscripts of the variables for the if block and the else block
		MutablePair<HashMap<String, Integer>, HashMap<String, Integer>> ifThenElseBranchedVariables = ifThenElseBranchStack
			.pop();

		// Separate the pair into their own HashMaps
		HashMap<String, Integer> ifBranchVariableSubscripts = ifThenElseBranchedVariables.left;
		HashMap<String, Integer> elseBranchVariableSubscripts = ifThenElseBranchedVariables.right;

		// Wrap a set of braces around the if block and the else block in case of single line statement
		for (Java8Parser.StatementNoShortIfContext statementNoShortIfContext : ctx.statementNoShortIf()) {
			rewriter.insertBefore(statementNoShortIfContext.getStart(), "{");
			// Iterate through each variable that was altered (in the entire if/else block that exists outside that scope), 
			// update version and record
			for (String var : predicateBlockVariablesStack.pop()) {
				// Insert recording function and variable version update after the if/else block for this variable
				insertVersionUpdateAfter(statementNoShortIfContext.getStart(), var);
				insertRecordStatementAfter(statementNoShortIfContext.getStart(), var, ctx.getStop().getLine());
			}
			rewriter.insertAfter(statementNoShortIfContext.getStop(), "}");
		}

		// Get variables from each block which experiences some change in assignment
		HashSet<String> ifBlockVariables = getAllAlteredVariablesFromContext(ctx.statementNoShortIf().get(0));
		HashSet<String> elseBlockVariables = getAllAlteredVariablesFromContext(ctx.statementNoShortIf().get(1));



		// Create a set for the variables from the if/else block that have been added to the causal map
		HashSet<String> allAddedVariables = new HashSet<>();

		// For each variable in the if block...
		for (String var : ifBlockVariables) {

			// If the variable was declared inside the block, continue
			if (!variableSubscripts.containsKey(var) || variablesDeclaredInPredicate.contains(var)
					|| !variablesInitializedInScope.lastElement().contains(var))
				continue;

			// Add the last variable subscript from the if block to the list of confounding adjustment variables 
			// for the merge variable after the if/else block
			causalMap.put(var + "_" + variableSubscripts.get(var), new HashSet<String>());
			causalMap.get(var + "_" + variableSubscripts.get(var)).add(var + "_" + ifBranchVariableSubscripts.get(var));

			// Add this variable to the list of variables that were added to the causal map
			allAddedVariables.add(var);
		}

		// Perform a similar process for the else block variables
		for (String var : elseBlockVariables) {

			// If the variable was declared inside the block, continue
			if (!variableSubscripts.containsKey(var) || variablesDeclaredInPredicate.contains(var)
					|| !variablesInitializedInScope.lastElement().contains(var))
				continue;

			// If the variable was not already added from the if block, add it now
			if (!causalMap.containsKey(var + "_" + variableSubscripts.get(var)))
				causalMap.put(var + "_" + variableSubscripts.get(var), new HashSet<String>());

			// Add the last variable subscript from the if block to the list of confounding adjustment variables 
			// for the merge variable after the if/else block
			causalMap.get(var + "_" + variableSubscripts.get(var)).add(var + "_" + elseBranchVariableSubscripts.get(var));

			// Add this variable to the list of variables that were added to the causal map
			allAddedVariables.add(var);
		}

		// If a variable was found only in one block of the if/else and not both, 
		// add the variable version before the if/else block to the list of confounding adjustment variables
		for (String var : allAddedVariables) {

			// If the variable was declared inside the block, continue
			if (!variableSubscripts.containsKey(var) || variablesDeclaredInPredicate.contains(var)
					|| !variablesInitializedInScope.lastElement().contains(var))
				continue;

			// If only one version was added to the causal map of a variable...
			if (causalMap.get(var + "_" + variableSubscripts.get(var)).size() == 1) {

				// Add the version that existed before the if/else statement
				causalMap.get(var + "_" + variableSubscripts.get(var)).add(var + "_" + varSubscriptsBeforePredicate.get(var));
			}
		}

	}

	@Override
	public void exitStatementNoShortIf(Java8Parser.StatementNoShortIfContext ctx) {
		ParserRuleContext parentContext = ctx.getParent();
		if (!(parentContext instanceof Java8Parser.IfThenElseStatementContext)
				&& !(parentContext instanceof Java8Parser.IfThenElseStatementNoShortIfContext))
			return;

		HashMap<String, Integer> currentVariableSubscripts = variableSubscripts;

		// Check if it is if branch or else branch
		// 1. Figure out which child of parent context it is
		for (int i = 0; i < parentContext.getChildCount(); i++) {
			if (parentContext.getChild(i) instanceof Java8Parser.StatementNoShortIfContext) {
				Java8Parser.StatementNoShortIfContext childCtx = (Java8Parser.StatementNoShortIfContext) parentContext
					.getChild(i);
				// If the child is the statement context
				if (childCtx.equals(ctx)) {
					// if block condition
					if (parentContext.getChild(i - 1).getText().equals(")")) {
						ifThenElseBranchStack.lastElement().setLeft(new HashMap<>(currentVariableSubscripts));
					} else if (parentContext.getChild(i - 1).getText().equals("else")) {
						ifThenElseBranchStack.lastElement().setRight(new HashMap<>(currentVariableSubscripts));
					}
				}
			}
		}
	}

	@Override
	public void exitStatement(Java8Parser.StatementContext ctx) {
		ParserRuleContext parentContext = ctx.getParent();
		if (!(parentContext instanceof Java8Parser.IfThenElseStatementContext)
				&& !(parentContext instanceof Java8Parser.IfThenElseStatementNoShortIfContext))
			return;
		HashMap<String, Integer> currentVariableSubscripts = variableSubscripts;
		ifThenElseBranchStack.lastElement().setRight(new HashMap<>(currentVariableSubscripts));
	}

	@Override
	public void enterWhileStatement(Java8Parser.WhileStatementContext ctx) {
	
		updateVariableSubscriptPredicateStack();

		variablesDeclaredInPredicateStack.push(new HashSet<String>());
		predicateBlockVariablesStack.push(new HashSet<String>());
	}

	@Override
	public void exitWhileStatement(Java8Parser.WhileStatementContext ctx) {
	
		HashMap<String, Integer> varSubscriptsBeforePredicate = varSubscriptsBeforePredicateStack.pop();
		HashSet<String> variablesDeclaredInPredicate = variablesDeclaredInPredicateStack.pop();

		// Get all variables from predicate
		ArrayList<String> expressionNamesList = getAllExpressionNamesFromPredicate(ctx.expression());

		// Replace predicate with true
		rewriter.replace(ctx.expression().getStart(), ctx.expression().getStop(), "true");

		// Get block context of while loop
		Java8Parser.BlockContext blockContext = ctx.statement().statementWithoutTrailingSubstatement().block();

		if (blockContext == null) {
			// Handling a single-statement while loop by wrapping braces around it

			// Insert breaking statement equivalent to negation of original predicate
			ArrayList<TerminalNode> expressionTokens = getAllTokensFromContext(ctx.expression());
			String expressionString = "";
			for (TerminalNode token : expressionTokens) {
				expressionString += token.getText() + " ";
			}
			
			rewriter.insertBefore(ctx.statement().statementWithoutTrailingSubstatement().getStart(),
					"if (!(" + expressionString + ")) {break;}");
			
			// Record predicate variable values
			for (String variable : expressionNamesList) {
				insertRecordStatementBefore(ctx.statement().statementWithoutTrailingSubstatement().getStart(), variable,
						ctx.expression().getStart().getLine());
				insertVersionUpdateBefore(ctx.statement().statementWithoutTrailingSubstatement().getStart(), variable);

			}

			// Wrap braces around the statement
			rewriter.insertBefore(ctx.statement().statementWithoutTrailingSubstatement().getStart(), "{");
			rewriter.insertAfter(ctx.statement().statementWithoutTrailingSubstatement().getStop(), "}");
		} else {
			Token blockContextStart = blockContext.getStart();
			Token blockContextStop = blockContext.getStop();

			ArrayList<TerminalNode> expressionTokens = getAllTokensFromContext(ctx.expression());
			String expressionString = "";
			for (TerminalNode token : expressionTokens) {
				expressionString += token.getText() + " ";
			}
			// Add breaking if statement equivalent to negation of original predicate
			if(!"true".equals(expressionString.trim())){
			
			rewriter.insertAfter(blockContextStart, "if (!(" + expressionString + ")) {break;}");
			
			}
			
			// Record predicate variable values
			for (String variable : expressionNamesList) {
				insertVersionUpdateAfter(blockContextStart, variable);
				insertRecordStatementAfter(blockContextStart, variable, ctx.expression().getStart().getLine());
			}
			
			
			
		}

		// Record values of all variables used inside the predicate block
		for (String variable : predicateBlockVariablesStack.pop()) {
			if (variablesDeclaredInPredicate.contains(variable) || !variableSubscripts.containsKey(variable)
					|| !variablesInitializedInScope.lastElement().contains(variable))
				continue;

			ArrayList<String> confounders = new ArrayList<>();
			confounders.add(variable + "_" + varSubscriptsBeforePredicate.get(variable));
			confounders.add(variable + "_" + variableSubscripts.get(variable));

			insertRecordStatementBefore(blockContext.getStop(), variable, ctx.getStop().getLine());
			insertVersionUpdateBefore(blockContext.getStop(), variable);


			if (variableSubscripts.containsKey(variable)) {
				causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
				causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(confounders);
			}
		}

	}

	@Override
	public void enterWhileStatementNoShortIf(Java8Parser.WhileStatementNoShortIfContext ctx) {
		updateVariableSubscriptPredicateStack();

		predicateBlockVariablesStack.push(new HashSet<String>());
		variablesDeclaredInPredicateStack.push(new HashSet<String>());
	}

	@Override
	public void exitWhileStatementNoShortIf(Java8Parser.WhileStatementNoShortIfContext ctx) {
		HashMap<String, Integer> varSubscriptsBeforePredicate = varSubscriptsBeforePredicateStack.pop();
		HashSet<String> variablesDeclaredInPredicate = variablesDeclaredInPredicateStack.pop();

		// Get all variables from predicate
		ArrayList<String> expressionNamesList = getAllExpressionNamesFromPredicate(ctx.expression());

		// Replace predicate with true
		rewriter.replace(ctx.expression().getStart(), ctx.expression().getStop(), "true");

		// Get block context of while loop
		Java8Parser.BlockContext blockContext = ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block();

		if (blockContext == null) {
			// Handling a single-statement while loop by wrapping braces around it
			ArrayList<TerminalNode> expressionTokens = getAllTokensFromContext(ctx.expression());
			String expressionString = "";
			for (TerminalNode token : expressionTokens) {
				expressionString += token.getText() + " ";
			}

			// Insert breaking statement equivalent to negation of original predicate
		
			rewriter.insertBefore(ctx.statementNoShortIf().statementWithoutTrailingSubstatement().getStart(),
					"if (!(" + expressionString + ")) {break;}");
			
			

			// Record predicate variable values
			for (String variable : expressionNamesList) {
				insertRecordStatementBefore(ctx.statementNoShortIf().statementWithoutTrailingSubstatement().getStart(),
						variable, ctx.expression().getStart().getLine());
				insertVersionUpdateBefore(ctx.statementNoShortIf().statementWithoutTrailingSubstatement().getStart(), variable);

			}

			// Wrap braces around the statement
			rewriter.insertBefore(ctx.statementNoShortIf().statementWithoutTrailingSubstatement().getStart(), "{");
			rewriter.insertAfter(ctx.statementNoShortIf().statementWithoutTrailingSubstatement().getStop(), "}");
		} else {
			Token blockContextStart = blockContext.getStart();
			Token blockContextStop = blockContext.getStop();

			ArrayList<TerminalNode> expressionTokens = getAllTokensFromContext(ctx.expression());
			String expressionString = "";
			for (TerminalNode token : expressionTokens) {
				expressionString += token.getText() + " ";
			}

			// Add breaking if statement equivalent to negation of original predicate
			if(!"true".equals(expressionString.trim())){
			
			rewriter.insertAfter(blockContextStart, "if (!(" + expressionString + ")) {break;}");
			
			}
			
			
			// Record predicate variable values
			for (String variable : expressionNamesList) {
				insertVersionUpdateAfter(blockContextStart, variable);
				insertRecordStatementAfter(blockContextStart, variable, ctx.expression().getStart().getLine());
			}
		}

		// Record values of all variables used inside the predicate block
		for (String variable : predicateBlockVariablesStack.pop()) {
			if (variablesDeclaredInPredicate.contains(variable) || !variableSubscripts.containsKey(variable)
					|| !variablesInitializedInScope.lastElement().contains(variable))
				continue;

			ArrayList<String> confounders = new ArrayList<>();
			confounders.add(variable + "_" + varSubscriptsBeforePredicate.get(variable));
			confounders.add(variable + "_" + variableSubscripts.get(variable));

			//      insertRecordStatementBefore(blockContext.getStop(), variable, ctx.getStop().getLine());
			//      insertVersionUpdateBefore(blockContext.getStop(), variable);


			if (variableSubscripts.containsKey(variable)) {
				causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
				causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(confounders);
			}
		}

	}

	// 1. Insert record statement for all updated variables inside loop before the break condition
	// 2. 
	Stack<HashSet<String>> phiEntryVariablesStack = new Stack<>();
	Stack<HashMap<String, String>> mergeVariablesStack = new Stack<>();

	@Override
	public void enterBasicForStatement(Java8Parser.BasicForStatementContext ctx) {
		updateVariableSubscriptPredicateStack();
		phiEntryVariablesStack.push(new HashSet<String>());
		predicateBlockVariablesStack.push(new HashSet<String>());
		variablesDeclaredInPredicateStack.push(new HashSet<String>());
		mergeVariablesStack.push(new HashMap<>());
		if (ctx.forInit() != null) {
			handleBasicForInit(ctx.forInit(), ctx);
		}

		if (ctx.expression() != null) {
			handleBasicForExpression(ctx);
		}
		HashSet<String> phiEntryVariables = phiEntryVariablesStack.pop();
		Java8Parser.BlockContext blockContext = ctx.statement().statementWithoutTrailingSubstatement().block();

		//System.out.println(currentMethodName);
		for (String variable : phiEntryVariables) {
			if (!variableSubscripts.containsKey(variable))
				continue;
			if (blockContext != null) {
				insertVersionUpdateAfter(ctx.statement().getStart(), variable);
				insertRecordStatementAfter(ctx.statement().getStart(), variable, ctx.expression().getStart().getLine());
			} else {
				insertRecordStatementBefore(ctx.statement().getStart(), variable, ctx.expression().getStart().getLine());
				insertVersionUpdateBefore(ctx.statement().getStart(), variable);

			}
			int version = variableSubscripts.get(variable);
			//System.out.println(variable + "_" + version);
			causalMap.put(variable + "_" + version, new HashSet<String>());
			causalMap.get(variable + "_" + version).add(variable + "_" + (version - 1));
			mergeVariablesStack.lastElement().put(variable, variable + "_" + version);
		}
	}

	@Override
	public void exitBasicForStatement(Java8Parser.BasicForStatementContext ctx) {
		HashMap<String, String> mergeVariables = mergeVariablesStack.pop();
		HashMap<String, Integer> varSubscriptsBeforePredicate = varSubscriptsBeforePredicateStack.pop();
		HashSet<String> variablesDeclaredInPredicate = variablesDeclaredInPredicateStack.pop();
		HashSet<String> forInitDeclarations = getAllForInitDeclarations(ctx.forInit());

		if (ctx.forUpdate() != null) {
			handleBasicForUpdate(ctx, mergeVariables);
		}

		if (ctx.statement().statementWithoutTrailingSubstatement().block() == null) {
			rewriter.insertBefore(ctx.statement().getStart(), "{");
			rewriter.insertAfter(ctx.statement().getStop(), "}");
		} else {
			String expressionString = "";
			if (ctx.expression() != null) {
				ArrayList<TerminalNode> expressionTokens = getAllTokensFromContext(ctx.expression());


				for (TerminalNode token : expressionTokens) {
					expressionString += token.getText() + " ";
				}
			}else{
				expressionString="true";
			}
			rewriter.insertAfter(ctx.statement().getStart(), "if (!(" + expressionString + ")) {break;}");
		}
		rewriter.insertBefore(ctx.getStart(), "{");
		rewriter.insertAfter(ctx.getStop(), "}");
		// System.out.println(ctx.getStart().getLine());
		//System.out.println("predicateBlockVariablesStack: " + predicateBlockVariablesStack);
		for (String variable : forInitDeclarations) {
			for (HashSet<String> predicateBlockVariables : predicateBlockVariablesStack) {
				if (predicateBlockVariables.contains(variable))
					predicateBlockVariables.remove(variable);
			}
		}

		for (String variable : predicateBlockVariablesStack.pop()) {
			if (!(forInitDeclarations.contains(variable))) {
				if (variablesDeclaredInPredicate.contains(variable) || !variableSubscripts.containsKey(variable)
						|| !variablesInitializedInScope.lastElement().contains(variable))
					continue;

				ArrayList<String> confounders = new ArrayList<>();
				int lineNumber = ctx.getStop().getLine();
				confounders.add(variable + "_" + varSubscriptsBeforePredicate.get(variable));
				confounders.add(variable + "_" + variableSubscripts.get(variable));
				// System.out.println(variable + "_" + varSubscriptsBeforePredicate.get(variable));
				// System.out.println(variable + "_" + variableSubscripts.get(variable));

				insertVersionUpdateAfter(ctx.statement().getStart(), variable);
				insertRecordStatementAfter(ctx.statement().getStart(), variable, lineNumber);
				// rewriter.insertAfter(ctx.getStop(),
				//   "// " + variable + " version: " + varSubscriptsBeforePredicate.get(variable));
				//  System.out.println(variable + "_" + variableSubscripts.get(variable));
				String mergeVar = variable + "_" + variableSubscripts.get(variable);

				// System.out.println(variable + "_" + variableSubscripts.get(variable));
				if (variableSubscripts.containsKey(variable)) {
					causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
					causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(confounders);
				}
				if ("determinant_2".equals(mergeVar))
					System.out.println(causalMap);


			}
		}
	}

	public HashSet<String> getAllForInitDeclarations(Java8Parser.ForInitContext ctx) {
		if (ctx == null)
			return new HashSet<String>();
		HashSet<String> forInitDeclarations = new HashSet<>();
		if (ctx.localVariableDeclaration() != null) {
			for (int i = 0; i < ctx.localVariableDeclaration().variableDeclaratorList().variableDeclarator().size(); i++) {
				Java8Parser.VariableDeclaratorIdContext varContext = ctx.localVariableDeclaration().variableDeclaratorList()
					.variableDeclarator(i).variableDeclaratorId();
				String variable = tokens.getText(varContext);
				// phiEntryVariablesStack.lastElement().add(variable);
				forInitDeclarations.add(variable);
			}
		}
		return forInitDeclarations;
	}

	@Override
	public void enterBasicForStatementNoShortIf(Java8Parser.BasicForStatementNoShortIfContext ctx) {
		updateVariableSubscriptPredicateStack();
		predicateBlockVariablesStack.push(new HashSet<String>());
		variablesDeclaredInPredicateStack.push(new HashSet<String>());
		phiEntryVariablesStack.push(new HashSet<String>());
		mergeVariablesStack.push(new HashMap<>());
		if (ctx.forInit() != null) {
			handleBasicForInit(ctx.forInit(), ctx);
		}
		
		if (ctx.expression() != null) {
			handleBasicForNoShortIfExpression(ctx);
		}
		HashSet<String> phiEntryVariables = phiEntryVariablesStack.pop();
		Java8Parser.BlockContext blockContext = ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block();

		for (String variable : phiEntryVariables) {
			if (!variableSubscripts.containsKey(variable))
				continue;
			if (blockContext != null) {
				insertVersionUpdateAfter(ctx.statementNoShortIf().getStart(), variable);
				insertRecordStatementAfter(ctx.statementNoShortIf().getStart(), variable,
						ctx.expression().getStart().getLine());
			} else {
				insertRecordStatementBefore(ctx.statementNoShortIf().getStart(), variable,ctx.expression().getStart().getLine());
				insertVersionUpdateBefore(ctx.statementNoShortIf().getStart(), variable);      
			}
			int version = variableSubscripts.get(variable);
			causalMap.put(variable + "_" + version, new HashSet<String>());
			causalMap.get(variable + "_" + version).add(variable + "_" + (version - 1));
			String mergeVar = variable + "_" + variableSubscripts.get(variable);
			mergeVariablesStack.lastElement().put(variable, variable + "_" + version);
		}
	}

	public void exitBasicForStatementNoShortIf(Java8Parser.BasicForStatementNoShortIfContext ctx) {
		HashMap<String, String> mergeVariables = mergeVariablesStack.pop();
		HashMap<String, Integer> varSubscriptsBeforePredicate = varSubscriptsBeforePredicateStack.pop();
		HashSet<String> variablesDeclaredInPredicate = variablesDeclaredInPredicateStack.pop();

		if (ctx.forUpdate() != null) {
			handleBasicForNoShortIfUpdate(ctx, mergeVariables);
		}

		if (ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block() == null) {
			rewriter.insertBefore(ctx.statementNoShortIf().getStart(), "{");
			rewriter.insertAfter(ctx.statementNoShortIf().getStop(), "}");
		} else {
			String expressionString = "";
			
			if (ctx.expression() != null) {
				ArrayList<TerminalNode> expressionTokens = getAllTokensFromContext(ctx.expression());


				for (TerminalNode token : expressionTokens) {
					expressionString += token.getText() + " ";
				}
			}else{
				expressionString="true";
			}
			rewriter.insertAfter(ctx.statementNoShortIf().getStart(), "if (!(" + expressionString + ")) {break;}");
		}
		rewriter.insertBefore(ctx.getStart(), "{");
		rewriter.insertAfter(ctx.getStop(), "}");

		HashSet<String> forInitDeclarations = getAllForInitDeclarations(ctx.forInit());
		for (String variable : predicateBlockVariablesStack.pop()) {
			if (!(forInitDeclarations.contains(variable))) {
				if (variablesDeclaredInPredicate.contains(variable) || !variableSubscripts.containsKey(variable)
						|| !variablesInitializedInScope.lastElement().contains(variable))
					continue;
				ArrayList<String> confounders = new ArrayList<>();
				confounders.add(variable + "_" + varSubscriptsBeforePredicate.get(variable));
				confounders.add(variable + "_" + variableSubscripts.get(variable));

				insertVersionUpdateAfter(ctx.statementNoShortIf().getStart(), variable);
				insertRecordStatementAfter(ctx.statementNoShortIf().getStart(), variable, ctx.getStop().getLine());
				//   rewriter.insertAfter(ctx.getStop(),
				//  "// " + variable + " version: " + varSubscriptsBeforePredicate.get(variable));

				// System.out.println(variable + "_" + variableSubscripts.get(variable));
				if (variableSubscripts.containsKey(variable)) {
					causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
					causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(confounders);
					String mergeVar = variable + "_" + variableSubscripts.get(variable);
				}

			}
		}
	}

	public void handleBasicForInit(Java8Parser.ForInitContext ctx, ParserRuleContext forCtx) {
		if (ctx.localVariableDeclaration() != null) {
			String forInit = "";
			ArrayList<TerminalNode> forInitTokens = getAllTokensFromContext(ctx.localVariableDeclaration());
			for (TerminalNode token : forInitTokens) {
				String tokenText = token.getText();
				forInit += tokenText + " ";
			}
			forInit += ";";
			String initializer = "";
			for (int i = 0; i < ctx.localVariableDeclaration().variableDeclaratorList().variableDeclarator().size(); i++) {
				Java8Parser.VariableDeclaratorIdContext varContext = ctx.localVariableDeclaration().variableDeclaratorList()
					.variableDeclarator(i).variableDeclaratorId();
				String variable = tokens.getText(varContext);
				int lineNumber = forCtx.getStart().getLine();
			}
			for (int i = 0; i < ctx.localVariableDeclaration().variableDeclaratorList().variableDeclarator().size(); i++) {
				Java8Parser.VariableDeclaratorIdContext varContext = ctx.localVariableDeclaration().variableDeclaratorList()
					.variableDeclarator(i).variableDeclaratorId();
				String variable = tokens.getText(varContext);
				int lineNumber = forCtx.getStart().getLine();
				if (ctx.localVariableDeclaration().variableDeclaratorList().variableDeclarator(i)
						.variableInitializer() != null) {
					if (!variableSubscripts.containsKey(variable))
						variableSubscripts.put(variable, -1);

					insertRecordStatementBefore(forCtx.getStart(), variable, lineNumber);
					insertVersionUpdateBefore(forCtx.getStart(), variable);

						}
			}
			rewriter.insertBefore(forCtx.getStart(), forInit);
			rewriter.insertBefore(forCtx.getStart(), initializer);

		} else if (ctx.statementExpressionList() != null) {

			for (int i = 0; i < ctx.statementExpressionList().statementExpression().size(); i++) {

				// Get for loop initializer
				String forInit = "";
				ArrayList<TerminalNode> forInitTokens = getAllTokensFromContext(
						ctx.statementExpressionList().statementExpression(i));
				for (TerminalNode token : forInitTokens) {
					String tokenText = token.getText();
					forInit += tokenText + " ";
				}
				forInit += ";";
				// Expression can only be one of assignment, preIncrementExpression, preDecrementExpression, 
				// postIncrementExpression, postDecrementExpression, methodInvocation, classInstanceCreationExpression;
				ParserRuleContext expressionContext = (ParserRuleContext) ctx.statementExpressionList().statementExpression(i)
					.getChild(0);
				// Handle assignment
				if (expressionContext instanceof Java8Parser.AssignmentContext) {
					Java8Parser.AssignmentContext assignmentContext = (Java8Parser.AssignmentContext) expressionContext;
					String variable = assignmentContext.leftHandSide().getText();
					int lineNumber = assignmentContext.getStart().getLine();
					HashSet<String> postFixAlteredVariables = getIncrementAndDecrementVariablesFromAssignment(assignmentContext);
					for (String alteredVariable : postFixAlteredVariables) {
						insertRecordStatementBefore(forCtx.getStart(), alteredVariable, lineNumber);
						insertVersionUpdateBefore(forCtx.getStart(), alteredVariable);

					}
					insertRecordStatementBefore(forCtx.getStart(), variable, lineNumber);
					insertVersionUpdateBefore(forCtx.getStart(), variable);

				}

				if (expressionContext instanceof Java8Parser.PreIncrementExpressionContext
						|| expressionContext instanceof Java8Parser.PreDecrementExpressionContext) {
					String variable = expressionContext.getChild(1).getText();
					int lineNumber = expressionContext.getStart().getLine();
					insertRecordStatementBefore(forCtx.getStart(), variable, lineNumber);
					insertVersionUpdateBefore(forCtx.getStart(), variable);

						}

				if (expressionContext instanceof Java8Parser.PostIncrementExpressionContext
						|| expressionContext instanceof Java8Parser.PostDecrementExpressionContext) {
					if (expressionContext.getChild(0).getChild(0) instanceof Java8Parser.ExpressionNameContext) {
						String variable = expressionContext.getChild(0).getChild(0).getText();
						int lineNumber = expressionContext.getStart().getLine();
						insertRecordStatementBefore(forCtx.getStart(), variable, lineNumber);
						insertVersionUpdateBefore(forCtx.getStart(), variable);

					}
						}
				rewriter.insertBefore(forCtx.getStart(), forInit);

			}
		}
	}

	public void handleBasicForUpdate(Java8Parser.BasicForStatementContext ctx, HashMap<String, String> mergeVariables) {
		ArrayList<Java8Parser.ContinueStatementContext> continueContexts = getAllContinueStatementContextsFromForLoop(ctx);
		// System.out.println("Inside Update");

		for (int i = 0; i < ctx.forUpdate().statementExpressionList().statementExpression().size(); i++) {
			ParserRuleContext expressionContext = (ParserRuleContext) ctx.forUpdate().statementExpressionList()
				.statementExpression(i).getChild(0);
			// Handle assignment
			if (expressionContext instanceof Java8Parser.AssignmentContext) {
				Java8Parser.AssignmentContext assignmentContext = (Java8Parser.AssignmentContext) expressionContext;
				String variable = assignmentContext.leftHandSide().getText();
				int lineNumber = assignmentContext.getStart().getLine();

				ArrayList<String> expressionNames = getAllExpressionNamesFromPredicate(assignmentContext.expression());
				ArrayList<String> expressionNamesSSA = new ArrayList<>();
				for (String expressionName : expressionNames) {
					if (!variableSubscripts.keySet().isEmpty())
						if (variableSubscripts.containsKey(expressionName))
							expressionNamesSSA.add(expressionName + "_" + variableSubscripts.get(expressionName));
				}
				if (assignmentContext.assignmentOperator().getText() != "=") {
					expressionNamesSSA.add(variable + "_" + variableSubscripts.get(variable));
				}

				if (ctx.statement().statementWithoutTrailingSubstatement().block() == null) {
					rewriter.insertAfter(ctx.statement().getStop(), assignmentContext.getText() + ";");
					insertVersionUpdateAfter(ctx.statement().getStop(), variable);
					insertRecordStatementAfter(ctx.statement().getStop(), variable, lineNumber);
				} else {
					insertRecordStatementBefore(ctx.statement().getStop(), variable, lineNumber);
					insertVersionUpdateBefore(ctx.statement().getStop(), variable);

					rewriter.insertBefore(ctx.statement().getStop(), assignmentContext.getText() + ";");
				}
				if (mergeVariables.containsKey(variable)) {
					causalMap.get(mergeVariables.get(variable)).add(variable + "_" + variableSubscripts.get(variable));
				}

				if (variableSubscripts.containsKey(variable)) {
					causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
					causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(expressionNamesSSA);
					String mergeVar = variable + "_" + variableSubscripts.get(variable);
				}

				HashSet<String> postFixAlteredVariables = getIncrementAndDecrementVariablesFromAssignment(assignmentContext);
				for (String alteredVariable : postFixAlteredVariables) {
					insertVersionUpdateAfter(ctx.getStop(), alteredVariable);
					insertRecordStatementAfter(ctx.getStop(), alteredVariable, lineNumber);
				}
				for (Java8Parser.ContinueStatementContext continueContext : continueContexts) {
					for (String alteredVariable : postFixAlteredVariables) {
						insertRecordStatementBefore(continueContext.getStart(), alteredVariable, lineNumber);
						insertVersionUpdateBefore(continueContext.getStart(), alteredVariable);

					}
					insertRecordStatementBefore(continueContext.getStart(), variable, lineNumber);
					insertVersionUpdateBefore(continueContext.getStart(), variable);

					rewriter.insertBefore(ctx.statement().getStop(), expressionContext.getText() + ";");
					rewriter.insertBefore(continueContext.getStart(), "{");
					rewriter.insertAfter(continueContext.getStop(), "}");
				}
			}

			if (expressionContext instanceof Java8Parser.PreIncrementExpressionContext
					|| expressionContext instanceof Java8Parser.PreDecrementExpressionContext) {
				String variable = expressionContext.getChild(1).getText();
				int lineNumber = expressionContext.getStart().getLine();

				ArrayList<String> confounders = new ArrayList<>();
				if (variableSubscripts.containsKey(variable))
					confounders.add(variable + "_" + variableSubscripts.get(variable));
				if (ctx.statement().statementWithoutTrailingSubstatement().block() == null) {
					rewriter.insertAfter(ctx.statement().getStop(), expressionContext.getText() + ";");
					insertVersionUpdateAfter(ctx.statement().getStop(), variable);
					insertRecordStatementAfter(ctx.statement().getStop(), variable, lineNumber);
				} else {
					insertRecordStatementBefore(ctx.statement().getStop(), variable, lineNumber);
					insertVersionUpdateBefore(ctx.statement().getStop(), variable);

					rewriter.insertBefore(ctx.statement().getStop(), expressionContext.getText() + ";");
				}

				if (mergeVariables.containsKey(variable)) {
					causalMap.get(mergeVariables.get(variable)).add(variable + "_" + variableSubscripts.get(variable));
				}

				if (variableSubscripts.containsKey(variable)) {
					causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
					causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(confounders);
					String mergeVar = variable + "_" + variableSubscripts.get(variable);
				}

				for (Java8Parser.ContinueStatementContext continueContext : continueContexts) {
					insertRecordStatementBefore(continueContext.getStart(), variable, lineNumber);
					insertVersionUpdateBefore(continueContext.getStart(), variable);

					rewriter.insertBefore(continueContext.getStart(), expressionContext.getText() + ";");
					rewriter.insertBefore(continueContext.getStart(), "{");
					rewriter.insertAfter(continueContext.getStop(), "}");
				}
					}

			if (expressionContext instanceof Java8Parser.PostIncrementExpressionContext
					|| expressionContext instanceof Java8Parser.PostDecrementExpressionContext) {
				if (expressionContext.getChild(0).getChild(0) instanceof Java8Parser.ExpressionNameContext) {
					String variable = expressionContext.getChild(0).getChild(0).getText();
					int lineNumber = expressionContext.getStart().getLine();

					ArrayList<String> confounders = new ArrayList<>();
					if (variableSubscripts.containsKey(variable))
						confounders.add(variable + "_" + variableSubscripts.get(variable));
					if (ctx.statement().statementWithoutTrailingSubstatement().block() == null) {
						rewriter.insertAfter(ctx.statement().getStop(), expressionContext.getText() + ";");
						insertVersionUpdateAfter(ctx.statement().getStop(), variable);
						insertRecordStatementAfter(ctx.statement().getStop(), variable, lineNumber);
					} else {
						insertRecordStatementBefore(ctx.statement().getStop(), variable, lineNumber);
						insertVersionUpdateBefore(ctx.statement().getStop(), variable);

						rewriter.insertBefore(ctx.statement().getStop(), expressionContext.getText() + ";");
					}

					if (mergeVariables.containsKey(variable)) {
						causalMap.get(mergeVariables.get(variable)).add(variable + "_" + variableSubscripts.get(variable));
					}

					if (variableSubscripts.containsKey(variable)) {
						causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
						causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(confounders);
						String mergeVar = variable + "_" + variableSubscripts.get(variable);
					}

					for (Java8Parser.ContinueStatementContext continueContext : continueContexts) {
						insertRecordStatementBefore(continueContext.getStart(), variable, lineNumber);
						insertVersionUpdateBefore(continueContext.getStart(), variable);

						rewriter.insertBefore(continueContext.getStart(), expressionContext.getText() + ";");
						rewriter.insertBefore(continueContext.getStart(), "{");
						rewriter.insertAfter(continueContext.getStop(), "}");
					}
				}
					}
		}

	}

	public void handleBasicForNoShortIfUpdate(Java8Parser.BasicForStatementNoShortIfContext ctx,
			HashMap<String, String> mergeVariables) {
		ArrayList<Java8Parser.ContinueStatementContext> continueContexts = getAllContinueStatementContextsFromForLoop(ctx);

		for (int i = 0; i < ctx.forUpdate().statementExpressionList().statementExpression().size(); i++) {
			ParserRuleContext expressionContext = (ParserRuleContext) ctx.forUpdate().statementExpressionList()
				.statementExpression(i).getChild(0);
			// Handle assignment
			if (expressionContext instanceof Java8Parser.AssignmentContext) {
				Java8Parser.AssignmentContext assignmentContext = (Java8Parser.AssignmentContext) expressionContext;
				String variable = assignmentContext.leftHandSide().getText();
				int lineNumber = assignmentContext.getStart().getLine();

				ArrayList<String> expressionNames = getAllExpressionNamesFromPredicate(assignmentContext.expression());
				ArrayList<String> expressionNamesSSA = new ArrayList<>();
				for (String expressionName : expressionNames) {
					if (!variableSubscripts.keySet().isEmpty())
						if (variableSubscripts.containsKey(expressionName))
							expressionNamesSSA.add(expressionName + "_" + variableSubscripts.get(expressionName));
				}
				if (assignmentContext.assignmentOperator().getText() != "=") {
					expressionNamesSSA.add(variable + "_" + variableSubscripts.get(variable));
				}
				if (ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block() == null) {
					rewriter.insertAfter(ctx.statementNoShortIf().getStop(), assignmentContext.getText() + ";");
					insertVersionUpdateAfter(ctx.statementNoShortIf().getStop(), variable);
					insertRecordStatementAfter(ctx.statementNoShortIf().getStop(), variable, lineNumber);
				} else {
					insertRecordStatementBefore(ctx.statementNoShortIf().getStop(), variable, lineNumber);
					insertVersionUpdateBefore(ctx.statementNoShortIf().getStop(), variable);

					rewriter.insertBefore(ctx.statementNoShortIf().getStop(), assignmentContext.getText() + ";");
				}
				if (mergeVariables.containsKey(variable)) {
					causalMap.get(mergeVariables.get(variable)).add(variable + "_" + variableSubscripts.get(variable));
				}

				if (variableSubscripts.containsKey(variable)) {
					causalMap.put(variable + "_" + variableSubscripts.get(variable), new HashSet<String>());
					causalMap.get(variable + "_" + variableSubscripts.get(variable)).addAll(expressionNamesSSA);
					String mergeVar = variable + "_" + variableSubscripts.get(variable);
				}

				HashSet<String> postFixAlteredVariables = getIncrementAndDecrementVariablesFromAssignment(assignmentContext);
				for (String alteredVariable : postFixAlteredVariables) {
					insertVersionUpdateAfter(ctx.getStop(), alteredVariable);
					insertRecordStatementAfter(ctx.getStop(), alteredVariable, lineNumber);
				}

				for (Java8Parser.ContinueStatementContext continueContext : continueContexts) {
					for (String alteredVariable : postFixAlteredVariables) {
						insertRecordStatementBefore(continueContext.getStart(), alteredVariable, lineNumber);
						insertVersionUpdateBefore(continueContext.getStart(), alteredVariable);

					}
					insertRecordStatementBefore(continueContext.getStart(), variable, lineNumber);
					insertVersionUpdateBefore(continueContext.getStart(), variable);

					rewriter.insertBefore(continueContext.getStart(), expressionContext.getText() + ";");
					rewriter.insertBefore(continueContext.getStart(), "{");
					rewriter.insertAfter(continueContext.getStop(), "}");
				}
			}

			if (expressionContext instanceof Java8Parser.PreIncrementExpressionContext
					|| expressionContext instanceof Java8Parser.PreDecrementExpressionContext) {
				String variable = expressionContext.getChild(1).getText();
				int lineNumber = expressionContext.getStart().getLine();

				if (ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block() == null) {
					insertVersionUpdateBefore(ctx.statementNoShortIf().getStart(), variable);
					insertRecordStatementBefore(ctx.statementNoShortIf().getStart(), variable, lineNumber);
					rewriter.insertBefore(ctx.statementNoShortIf().getStart(), expressionContext.getText() + ";");
				} else {
					rewriter.insertAfter(ctx.statementNoShortIf().getStart(), expressionContext.getText() + ";");
					insertVersionUpdateAfter(ctx.statementNoShortIf().getStart(), variable);
					insertRecordStatementAfter(ctx.statementNoShortIf().getStart(), variable, lineNumber);
				}

				if (mergeVariables.containsKey(variable)) {
					causalMap.get(mergeVariables.get(variable)).add(variable + "_" + variableSubscripts.get(variable));
					String mergeVar = variable + "_" + variableSubscripts.get(variable);
					if ("determinant_2".equals(mergeVar))
						System.out.println("CHANGE");
				}
					}

			if (expressionContext instanceof Java8Parser.PostIncrementExpressionContext
					|| expressionContext instanceof Java8Parser.PostDecrementExpressionContext) {
				if (expressionContext.getChild(0).getChild(0) instanceof Java8Parser.ExpressionNameContext) {
					String variable = expressionContext.getChild(0).getChild(0).getText();
					int lineNumber = expressionContext.getStart().getLine();
					if (ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block() == null) {
						rewriter.insertAfter(ctx.statementNoShortIf().getStop(), expressionContext.getText() + ";");
						insertVersionUpdateAfter(ctx.statementNoShortIf().getStop(), variable);
						insertRecordStatementAfter(ctx.statementNoShortIf().getStop(), variable, lineNumber);
					} else {
						insertVersionUpdateBefore(ctx.statementNoShortIf().getStop(), variable);
						insertRecordStatementBefore(ctx.statementNoShortIf().getStop(), variable, lineNumber);
						rewriter.insertBefore(ctx.statementNoShortIf().getStop(), expressionContext.getText() + ";");
					}

					if (mergeVariables.containsKey(variable)) {
						causalMap.get(mergeVariables.get(variable)).add(variable + "_" + variableSubscripts.get(variable));
					}
					for (Java8Parser.ContinueStatementContext continueContext : continueContexts) {
						insertRecordStatementBefore(continueContext.getStart(), variable, lineNumber);
						insertVersionUpdateBefore(continueContext.getStart(), variable);

						rewriter.insertBefore(continueContext.getStart(), expressionContext.getText() + ";");
						rewriter.insertBefore(continueContext.getStart(), "{");
						rewriter.insertAfter(continueContext.getStop(), "}");
					}
				}
					}
		}
	}

	public void handleBasicForExpression(Java8Parser.BasicForStatementContext ctx) {
		Token endParenthesis = null;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i).getText().equals(")")) {
				TerminalNode node = (TerminalNode) ctx.getChild(i);
				endParenthesis = node.getSymbol();
			}
		}
		if (endParenthesis != null) {
			rewriter.replace(ctx.getStart(), endParenthesis, "while (true)");
		}
		ArrayList<String> expressionNamesList = getAllExpressionNamesFromPredicate(ctx.expression());
		Java8Parser.BlockContext blockContext = ctx.statement().statementWithoutTrailingSubstatement().block();

		if (blockContext != null) {
			for (String variable : expressionNamesList) {
				phiEntryVariablesStack.lastElement().add(variable);
			}
		} else {
			String expressionString = "";
			
			if (ctx.expression() != null) {
				ArrayList<TerminalNode> expressionTokens = getAllTokensFromContext(ctx.expression());


				for (TerminalNode token : expressionTokens) {
					expressionString += token.getText() + " ";
				}
			}else{
				expressionString="true";
			}
			rewriter.insertBefore(ctx.statement().getStart(), "if (!(" + expressionString + ")) {break;}");
			for (String variable : expressionNamesList) {
				phiEntryVariablesStack.lastElement().add(variable);
			}
		}
	}

	public void handleBasicForNoShortIfExpression(Java8Parser.BasicForStatementNoShortIfContext ctx) {
		Token endParenthesis = null;
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i).getText().equals(")")) {
				TerminalNode node = (TerminalNode) ctx.getChild(i);
				endParenthesis = node.getSymbol();
			}
		}
		if (endParenthesis != null) {
			rewriter.replace(ctx.getStart(), endParenthesis, "while (true)");
		}
		ArrayList<String> expressionNamesList = getAllExpressionNamesFromPredicate(ctx.expression());
		Java8Parser.BlockContext blockContext = ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block();

		// if (blockContext != null) {
		//   for (String variable : expressionNamesList) {
		//     insertVersionUpdateAfter(ctx.statementNoShortIf().getStart(), variable);
		//     insertRecordStatementAfter(ctx.statementNoShortIf().getStart(), variable,
		//         ctx.expression().getStart().getLine());
		//   }
		// } else {
		//   rewriter.insertBefore(ctx.statementNoShortIf().getStart(), "if (!(" + ctx.expression().getText() + ")) {break;}");
		//   for (String variable : expressionNamesList) {
		//     insertRecordStatementBefore(ctx.statementNoShortIf().getStart(), variable,
		//         ctx.expression().getStart().getLine());
		//     insertVersionUpdateBefore(ctx.statementNoShortIf().getStart(), variable);
		//   }
		// }
		if (blockContext != null) {
			for (String variable : expressionNamesList) {
				phiEntryVariablesStack.lastElement().add(variable);
			}
		} else {
			String expressionString = "";
			
			if (ctx.expression() != null) {
				ArrayList<TerminalNode> expressionTokens = getAllTokensFromContext(ctx.expression());


				for (TerminalNode token : expressionTokens) {
					expressionString += token.getText() + " ";
				}
			}else{
				expressionString="true";
			}
			rewriter.insertBefore(ctx.statementNoShortIf().getStart(), "if (!(" + expressionString + ")) {break;}");
			for (String variable : expressionNamesList) {
				phiEntryVariablesStack.lastElement().add(variable);
			}
		}
	}

	//  @Override
	//  public void enterEnhancedForStatement(Java8Parser.EnhancedForStatementContext ctx) {
	//    predicateBlockVariablesStack.push(new HashSet<String>());
	//    variablesDeclaredInPredicateStack.push(new HashSet<String>());
	//  }
	//
	//  @Override
	//  public void exitEnhancedForStatement(Java8Parser.EnhancedForStatementContext ctx) {
	//    variablesDeclaredInPredicateStack.pop();
	//    TerminalNode forNode = (TerminalNode) ctx.getChild(0);
	//    TerminalNode startParenthesis = (TerminalNode) ctx.getChild(1);
	//    TerminalNode endParenthesis = (TerminalNode) ctx.getChild(ctx.getChildCount() - 2);
	//
	//    String iterableName = ctx.expression().getText();
	//    String iteratorType = ctx.unannType().getText();
	//    String iteratorItem = ctx.variableDeclaratorId().getText();
	//
	//    if (!variableSubscripts.containsKey(iteratorItem)) {
	//      rewriter.insertBefore(forNode.getSymbol(), "int " + iteratorItem + "_version = -1;");
	//      variableSubscripts.put(iteratorItem, -1);
	//    } else {
	//      if (allLocalVariables.contains(iteratorItem))
	//        rewriter.insertBefore(forNode.getSymbol(),
	//           iteratorItem + "_version"+ variableSubscripts.get(iteratorItem)+" = " + variableSubscripts.get(iteratorItem) + ";");
	//      else
	//        rewriter.insertBefore(forNode.getSymbol(),
	//            "int " + iteratorItem + "_version" +variableSubscripts.get(iteratorItem) +"= " + variableSubscripts.get(iteratorItem) + ";");
	//
	//    }
	//
	//    rewriter.insertBefore(forNode.getSymbol(),
	//        "java.util.Iterator<" + iteratorType + ">" + iterableName + "_iterator = " + iterableName + ".iterator();");
	//
	//    if (ctx.statement().statementWithoutTrailingSubstatement().block() == null) {
	//     insertRecordStatementBefore(ctx.statement().getStart(), iteratorItem, ctx.statement().getStart().getLine());
	//      insertVersionUpdateBefore(ctx.statement().getStart(), iteratorItem);
	//
	//      rewriter.insertBefore(ctx.statement().getStart(),
	//          iteratorType + " " + iteratorItem + "=" + iterableName + "_iterator" + ".next();");
	//    } else {
	//      rewriter.insertAfter(ctx.statement().getStart(),
	//          iteratorType + " " + iteratorItem + "=" + iterableName + "_iterator" + ".next();");
	//      insertVersionUpdateAfter(ctx.statement().getStart(), iteratorItem);
	//      insertRecordStatementAfter(ctx.statement().getStart(), iteratorItem, ctx.statement().getStart().getLine());
	//    }
	//
	//    rewriter.replace(forNode.getSymbol(), "while");
	//    rewriter.replace(startParenthesis.getSymbol(), endParenthesis.getSymbol(),
	//        "(" + iterableName + "_iterator" + ".hasNext())");
	//
	//    if (ctx.statement().statementWithoutTrailingSubstatement().block() == null) {
	//      rewriter.insertBefore(ctx.statement().getStart(), "{");
	//      rewriter.insertAfter(ctx.statement().getStop(), "}");
	//    }
	//
	//    rewriter.insertBefore(ctx.getStart(), "{");
	//    rewriter.insertAfter(ctx.getStop(), "}");
	//
	//    for (String variable : predicateBlockVariablesStack.pop()) {
	//      // if (!(forInitDeclarations.contains(variable))) {
	//      insertVersionUpdateAfter(ctx.getStop(), variable);
	//      insertRecordStatementAfter(ctx.getStop(), variable, ctx.getStop().getLine());
	//      // }
	//    }
	//  }
	//
	//  @Override
	//  public void enterEnhancedForStatementNoShortIf(Java8Parser.EnhancedForStatementNoShortIfContext ctx) {
	//    predicateBlockVariablesStack.push(new HashSet<String>());
	//    variablesDeclaredInPredicateStack.push(new HashSet<String>());
	//  }
	//
	//  @Override
	//  public void exitEnhancedForStatementNoShortIf(Java8Parser.EnhancedForStatementNoShortIfContext ctx) {
	//    variablesDeclaredInPredicateStack.pop();
	//    TerminalNode forNode = (TerminalNode) ctx.getChild(0);
	//    TerminalNode startParenthesis = (TerminalNode) ctx.getChild(1);
	//    TerminalNode endParenthesis = (TerminalNode) ctx.getChild(ctx.getChildCount() - 2);
	//
	//    String iterableName = ctx.expression().getText();
	//    String iteratorType = ctx.unannType().getText();
	//    String iteratorItem = ctx.variableDeclaratorId().getText();
	//
	//    if (!variableSubscripts.containsKey(iteratorItem)) {
	//      rewriter.insertBefore(forNode.getSymbol(), "int " + iteratorItem + "_version = -1;");
	//      variableSubscripts.put(iteratorItem, -1);
	//    } else {
	//      if (allLocalVariables.contains(iteratorItem))
	//        rewriter.insertBefore(forNode.getSymbol(),
	//             iteratorItem + "_version"+ variableSubscripts.get(iteratorItem)+" = " + variableSubscripts.get(iteratorItem) + ";");
	//      else
	//        rewriter.insertBefore(forNode.getSymbol(),
	//            "int "+ iteratorItem + "_version"+ variableSubscripts.get(iteratorItem)+" = " + variableSubscripts.get(iteratorItem) + ";");
	//    }
	//
	//    rewriter.insertBefore(forNode.getSymbol(),
	//        "java.util.Iterator<" + iteratorType + ">" + iterableName + "_iterator = " + iterableName + ".iterator();");
	//
	//    if (ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block() == null) {
	//      insertRecordStatementBefore(ctx.statementNoShortIf().getStart(), iteratorItem,ctx.statementNoShortIf().getStart().getLine());
	//      insertVersionUpdateBefore(ctx.statementNoShortIf().getStart(), iteratorItem);
	//
	//
	//      rewriter.insertBefore(ctx.statementNoShortIf().getStart(),
	//          iteratorType + " " + iteratorItem + "=" + iterableName + "_iterator" + ".next();");
	//    } else {
	//      rewriter.insertAfter(ctx.statementNoShortIf().getStart(),
	//          iteratorType + " " + iteratorItem + "=" + iterableName + "_iterator" + ".next();");
	//      insertVersionUpdateAfter(ctx.statementNoShortIf().getStart(), iteratorItem);
	//      insertRecordStatementAfter(ctx.statementNoShortIf().getStart(), iteratorItem,
	//          ctx.statementNoShortIf().getStart().getLine());
	//    }
	//
	//    rewriter.replace(forNode.getSymbol(), "while");
	//    rewriter.replace(startParenthesis.getSymbol(), endParenthesis.getSymbol(),
	//        "(" + iterableName + "_iterator" + ".hasNext())");
	//
	//    if (ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block() == null) {
	//      rewriter.insertBefore(ctx.statementNoShortIf().getStart(), "{");
	//      rewriter.insertAfter(ctx.statementNoShortIf().getStop(), "}");
	//    }
	//
	//    rewriter.insertBefore(ctx.getStart(), "{");
	//    rewriter.insertAfter(ctx.getStop(), "}");
	//
	//    for (String variable : predicateBlockVariablesStack.pop()) {
	//      // if (!(forInitDeclarations.contains(variable))) {
	//      insertVersionUpdateAfter(ctx.getStop(), variable);
	//      insertRecordStatementAfter(ctx.getStop(), variable, ctx.getStop().getLine());
	//      // }
	//    }
	//  }

	public ArrayList<Java8Parser.ContinueStatementContext> getAllContinueStatementContextsFromForLoop(
			ParserRuleContext ctx) {
		ArrayList<Java8Parser.ContinueStatementContext> continueContexts = new ArrayList<>();

		int numChildren = ctx.getChildCount();
		for (int i = 0; i < numChildren; i++) {
			if (ctx.getChild(i) instanceof Java8Parser.ContinueStatementContext) {
				continueContexts.add((Java8Parser.ContinueStatementContext) ctx.getChild(i));
			} else if (ctx.getChild(i) instanceof ParserRuleContext) {
				continueContexts.addAll(getAllContinueStatementContextsFromForLoop((ParserRuleContext) ctx.getChild(i)));
			}
		}

		return continueContexts;
			}

	public HashSet<String> getAllAlteredVariablesFromContext(ParserRuleContext ctx) {
		HashSet<String> allAlteredVariables = new HashSet<>();

		int numChildren = ctx.getChildCount();
		for (int i = 0; i < numChildren; i++) {
			if (ctx instanceof Java8Parser.AssignmentContext) {
				Java8Parser.AssignmentContext assignmentCtx = (Java8Parser.AssignmentContext) ctx;
				allAlteredVariables.add(assignmentCtx.leftHandSide().getText());
				allAlteredVariables.addAll(getIncrementAndDecrementVariablesFromAssignment(assignmentCtx));
			} else if (ctx.getChild(i) instanceof Java8Parser.PreIncrementExpressionContext) {
				Java8Parser.PreIncrementExpressionContext expr = (Java8Parser.PreIncrementExpressionContext) ctx.getChild(i);
				allAlteredVariables.add(expr.unaryExpression().getText());
			} else if (ctx.getChild(i) instanceof Java8Parser.PreDecrementExpressionContext) {
				Java8Parser.PreDecrementExpressionContext expr = (Java8Parser.PreDecrementExpressionContext) ctx.getChild(i);
				allAlteredVariables.add(expr.unaryExpression().getText());
			} else if (ctx.getChild(i) instanceof Java8Parser.PostIncrementExpressionContext) {
				Java8Parser.PostIncrementExpressionContext expr = (Java8Parser.PostIncrementExpressionContext) ctx.getChild(i);
				String varName = tokens.getText(expr.postfixExpression().expressionName());
				allAlteredVariables.add(varName);
			} else if (ctx.getChild(i) instanceof Java8Parser.PostDecrementExpressionContext) {
				Java8Parser.PostDecrementExpressionContext expr = (Java8Parser.PostDecrementExpressionContext) ctx.getChild(i);
				String varName = tokens.getText(expr.postfixExpression().expressionName());
				allAlteredVariables.add(varName);
			} else if (ctx.getChild(i) instanceof ParserRuleContext) {
				allAlteredVariables.addAll(getAllAlteredVariablesFromContext((ParserRuleContext) ctx.getChild(i)));
			}
		}

		return allAlteredVariables;
	}

	public void insertVersionUpdateAfter(Token token, String variableName) {
		if (!variableSubscripts.isEmpty()) {
			if (!variableSubscripts.containsKey(variableName))
				return;
			int version = variableSubscripts.get(variableName);
			if (variableSubscripts.keySet().contains(variableName)) {
				variableSubscripts.put(variableName, version + 1);
			}

			// System.out.println(token.getText());
			// System.out.println(variableName + " " + variableSubscripts.get(variableName));
			//rewriter.insertAfter(token, "int "+variableName + "_version"+variableSubscripts.get(variableName)+" = " + variableSubscripts.get(variableName) + ";");
		}
	}

	public void insertVersionUpdateBefore(Token token, String variableName) {
		if (!variableSubscripts.isEmpty()) {
			if (!variableSubscripts.containsKey(variableName))
				return;
			int version = variableSubscripts.get(variableName);

			if (variableSubscripts.keySet().contains(variableName)) {
				variableSubscripts.put(variableName, version + 1);
			}

			//rewriter.insertBefore(token, "int "+variableName + "_version"+variableSubscripts.get(variableName)+" = " + variableSubscripts.get(variableName) + ";");
		}
	}

	public void insertRecordStatementAfter(Token token, String variableName, int lineNumber) {
		if (!variableSubscripts.containsKey(variableName))
			return;
		String variableInQuotes = "\"" + variableName + "\"";
		String packageNameInQuotes = "\"" + packageName + "\"";
		String classNameInQuotes = "\"" + className + "\"";
		String methodNameInQuotes = "\"" + currentMethodName + "\"";
		String variableVersionCounter =String.valueOf(variableSubscripts.get(variableName));
		rewriter.insertAfter(token,
				"CollectOut.record(" + packageNameInQuotes + "," + classNameInQuotes + "," + methodNameInQuotes + "," + lineNumber + ","
				+ scope + "," + variableInQuotes + "," + variableName + "," + variableVersionCounter + ");");
	}

	public void insertRecordStatementAfter(Token token, String variableName, int lineNumber , int versionAltered) {
		if (!variableSubscripts.containsKey(variableName))
			return;
		String variableInQuotes = "\"" + variableName + "\"";
		String packageNameInQuotes = "\"" + packageName + "\"";
		String classNameInQuotes = "\"" + className + "\"";
		String methodNameInQuotes = "\"" + currentMethodName + "\"";
		String variableVersionCounter = String.valueOf(versionAltered);
		rewriter.insertAfter(token,
				"CollectOut.record(" + packageNameInQuotes + "," + classNameInQuotes + "," + methodNameInQuotes + "," + lineNumber + ","
				+ scope + "," + variableInQuotes + "," + variableName + "," + variableVersionCounter + ");");
	}

	public void insertRecordStatementBefore(Token token, String variableName, int lineNumber) {
		if (!variableSubscripts.containsKey(variableName))
			return;
		String variableInQuotes = "\"" + variableName + "\"";
		String packageNameInQuotes = "\"" + packageName + "\"";
		String classNameInQuotes = "\"" + className + "\"";
		String methodNameInQuotes = "\"" + currentMethodName + "\"";
		int insertnumber=variableSubscripts.get(variableName)+1;
		String variableVersionCounter = String.valueOf(insertnumber);
		rewriter.insertBefore(token,
				"CollectOut.record(" + packageNameInQuotes + "," + classNameInQuotes + "," + methodNameInQuotes + "," + lineNumber + ","
				+ scope + "," + variableInQuotes + "," + variableName + "," + variableVersionCounter + ");");
	}

	public ArrayList<String> getAllExpressionNamesFromPredicate(ParserRuleContext expressionContext) {

		ArrayList<String> expressionNamesList = new ArrayList<>();

		int numChildren = expressionContext.getChildCount();
		for (int i = 0; i < numChildren; i++) {
			if (expressionContext.getChild(i) instanceof Java8Parser.ExpressionNameContext) {
				expressionNamesList.add(expressionContext.getChild(i).getText());
			} else if (expressionContext.getChild(i) instanceof ParserRuleContext) {
				expressionNamesList
					.addAll(getAllExpressionNamesFromPredicate((ParserRuleContext) expressionContext.getChild(i)));
			}
		}
		return expressionNamesList;
	}

	public boolean insidePredicate(ParserRuleContext ctx) {
		while (ctx.getParent().getParent() != null) {
			if (Java8Parser.ExpressionContext.class.isInstance(ctx.getParent())
					&& (Java8Parser.IfThenStatementContext.class.isInstance(ctx.getParent().getParent())
						|| Java8Parser.WhileStatementContext.class.isInstance(ctx.getParent().getParent())
						|| Java8Parser.WhileStatementNoShortIfContext.class.isInstance(ctx.getParent().getParent())
						|| Java8Parser.BasicForStatementContext.class.isInstance(ctx.getParent().getParent())
						|| Java8Parser.BasicForStatementNoShortIfContext.class.isInstance(ctx.getParent().getParent())
						|| Java8Parser.IfThenElseStatementContext.class.isInstance(ctx.getParent().getParent())
						|| Java8Parser.IfThenElseStatementNoShortIfContext.class.isInstance(ctx.getParent().getParent()))) {
				return true;
						}
			ctx = ctx.getParent();
		}
		return false;
	}

	public boolean insidePredicateBlock(ParserRuleContext ctx) {
		while (ctx.getParent() != null) {
			if (Java8Parser.IfThenStatementContext.class.isInstance(ctx.getParent().getParent())
					|| Java8Parser.WhileStatementContext.class.isInstance(ctx.getParent().getParent())
					|| Java8Parser.WhileStatementNoShortIfContext.class.isInstance(ctx.getParent().getParent())
					|| Java8Parser.IfThenElseStatementContext.class.isInstance(ctx.getParent().getParent())
					|| Java8Parser.IfThenElseStatementNoShortIfContext.class.isInstance(ctx.getParent().getParent())
					|| Java8Parser.ForStatementContext.class.isInstance(ctx.getParent().getParent())
					|| Java8Parser.ForStatementNoShortIfContext.class.isInstance(ctx.getParent().getParent())
					|| Java8Parser.EnhancedForStatementContext.class.isInstance(ctx.getParent().getParent())
					|| Java8Parser.EnhancedForStatementNoShortIfContext.class.isInstance(ctx.getParent().getParent())) {
				return true;
					}
			ctx = ctx.getParent();
		}
		return false;
	}

	public void updateVariableSubscriptPredicateStack() {
		HashMap<String, Integer> varSubscriptsBeforePredicate = new HashMap<>(variableSubscripts);
		varSubscriptsBeforePredicateStack.push(varSubscriptsBeforePredicate);
	}

	// Get all tokens in a given context
	public ArrayList<TerminalNode> getAllTokensFromContext(ParserRuleContext ctx) {
		ArrayList<TerminalNode> terminalNodes = new ArrayList<>();

		int numChildren = ctx.getChildCount();
		//System.out.println(numChildren);

		for (int i = 0; i < numChildren; i++) {
			if (ctx.getChild(i) instanceof TerminalNode) {
				terminalNodes.add((TerminalNode) ctx.getChild(i));
			} else if (ctx.getChild(i) instanceof ParserRuleContext) {
				terminalNodes.addAll(getAllTokensFromContext((ParserRuleContext) ctx.getChild(i)));
			}
		}

		return terminalNodes;
	}

	public HashSet<String> getIncrementAndDecrementVariablesFromAssignment(ParserRuleContext ctx) {
		HashSet<String> variables = new HashSet<>();

		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i) instanceof Java8Parser.PostIncrementExpression_lf_postfixExpressionContext) {
				// Get post increment variables
				Java8Parser.PostIncrementExpression_lf_postfixExpressionContext postfixExprCtx = (Java8Parser.PostIncrementExpression_lf_postfixExpressionContext) ctx
					.getChild(i);
				Java8Parser.PostfixExpressionContext parent = (Java8Parser.PostfixExpressionContext) postfixExprCtx.getParent();
				if (parent.expressionName() != null) {
					variables.add(parent.expressionName().getText());
				}

			} else if (ctx.getChild(i) instanceof Java8Parser.PostDecrementExpression_lf_postfixExpressionContext) {
				// Get post decrement variables
				Java8Parser.PostDecrementExpression_lf_postfixExpressionContext postfixExprCtx = (Java8Parser.PostDecrementExpression_lf_postfixExpressionContext) ctx
					.getChild(i);
				Java8Parser.PostfixExpressionContext parent = (Java8Parser.PostfixExpressionContext) postfixExprCtx.getParent();
				if (parent.expressionName() != null) {
					variables.add(parent.expressionName().getText());
				}

			} else if (ctx.getChild(i) instanceof Java8Parser.PreIncrementExpressionContext) {
				Java8Parser.PreIncrementExpressionContext expr = (Java8Parser.PreIncrementExpressionContext) ctx.getChild(i);
				variables.add(expr.unaryExpression().getText());

			} else if (ctx.getChild(i) instanceof Java8Parser.PreDecrementExpressionContext) {
				Java8Parser.PreDecrementExpressionContext expr = (Java8Parser.PreDecrementExpressionContext) ctx.getChild(i);
				variables.add(expr.unaryExpression().getText());
			}

			else if (ctx.getChild(i) instanceof ParserRuleContext) {
				variables.addAll(getIncrementAndDecrementVariablesFromAssignment((ParserRuleContext) ctx.getChild(i)));
			}
		}
		return variables;
	}

	// Checks whether a given context is the descendant of another given context
	public boolean isDescendantOf(ParserRuleContext ctx, Class cls) {
		while (ctx.getParent() != null) {
			if (cls.isInstance(ctx.getParent())) {
				return true;
			}
			ctx = ctx.getParent();
		}
		return false;
	}
}
