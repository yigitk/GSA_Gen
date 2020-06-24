
# 3 RF
predCFoutRF <- function(dataFrame, outVarName, treatVarName, treatVal) {
  library("ranger");
  forest <- ranger(paste(outVarName, " ~ .", sep = ""), data = dataFrame);
  CFdata <- data.frame(dataFrame);
  CFdata[[treatVarName]] <- rep(treatVal, length(CFdata[[treatVarName]]));
  CFout <- predictions(predict(forest, CFdata))
  summary(forest)
  return(CFout);

}

# 3 LM
predCFoutLM <- function(dataFrame, outVarName, treatVarName,  treatVal) {

  M <- lm(paste(outVarName, " ~ .", sep=""), data=dataFrame);
  CFdata <- data.frame(dataFrame);
  CFdata[[treatVarName]] <- rep(treatVal, length(CFdata[[treatVarName]]));
  CFout <- predict(M, CFdata);
  return(CFout);
}

# 3 predict with lasso
predCFoutLM <- function(dataFrame, outVarName, treatVarName,  treatVal) {
  library(glmnet)
  # newoutput <- apply(newoutput, 2, as.numeric)
  # Y <- apply(Y, 2, as.numeric)
  fit.glmnet <- cv.glmnet(x=data.matrix(dataFrame[,-1]), data.matrix(dataFrame[,1]), family = "gaussian")

  CFdata <- data.frame(dataFrame);
  CFdata[[treatVarName]] <- rep(treatVal, length(CFdata[[treatVarName]]));

  # CFdata <- apply(CFdata, 2, as.numeric)
  CFout <- predict(fit.glmnet, newx = data.matrix(CFdata[,-1]), s="lambda.min", type='response');
  # return(CFout);
}

predCFprobRF <- function(dataFrame, outVarName, treatVarName, treatVal) {

  library("ranger");
  forest <- ranger(paste(outVarName, " ~ .", sep=""), data=dataFrame, probability=TRUE);
  CFdata <- data.frame(dataFrame);
  CFdata[[treatVarName]] <- rep(treatVal, length(CFdata[[treatVarName]]));
  CFout <- predictions(predict(forest, data=CFdata))
  return(CFout[, 2])
}

# 0
trainCFoutPredRF <- function(dataFrame, outVarName) {

  library("ranger");
  forest <- ranger(paste(outVarName, " ~ .", sep=""), data=dataFrame);
  return(forest);
}

trainCFprobPredRF <- function(dataFrame, outVarName) {

  library("ranger");
  forest <- ranger(paste(outVarName, " ~ .", sep=""), data=dataFrame, probability=TRUE);
  return(forest);
}

CFmeansForTreatRangeRF <- function(dataFrame, outVarName, treatVarName, treatVec, minTreat, maxTreat) {

  CFmeans <- rep(0, maxTreat - minTreat + 1);

  for (i in minTreat:maxTreat){
    CFmeans[i] <- mean(predCFoutRF(dataFrame, outVarName, treatVarName, treatVec[i]));
  }

  return(CFmeans);

}

# 2 RF
CFmeansForTreatVecRF <- function(dataFrame, outVarName, treatVarName, treatVec) {

  CFmeans <- rep(0, length(treatVec));

  for (i in 1:length(treatVec)) {
    CFmeans[i] <- mean(predCFoutRF(dataFrame, outVarName, treatVarName, treatVec[i]));
  }

  return(CFmeans);

}

# 2 LM
CFmeansForTreatVecLM <- function(dataFrame, outVarName, treatVarName, treatVec) {

  CFmeans <- rep(0, length(treatVec));

  for (i in 1:length(treatVec)) {
    CFmeans[i] <- mean(predCFoutLM(dataFrame, outVarName, treatVarName, treatVec[i]));
  }

  return(CFmeans);

}


CFprobsForTreatVecRF <- function(dataFrame, outVarName, treatVarName, treatVec) {


  CFprobs <- rep(0, length(treatVec));

  for (i in 1:length(treatVec)) {
    CFprobs[i] <- mean(predCFprobRF(dataFrame, outVarName, treatVarName, treatVec[i]));
  }

  return(CFprobs);

}

# 1 RF
CFmeansForDecileBinsRF <- function(dataFrame, outVarName, treatVarName) {

  fivePercentQuantiles <- quantile(dataFrame[[treatVarName]], prob = seq(0, 1, length = 21), type = 5, na.rm = TRUE)
  # Define the bins
  evenQuantiles <- fivePercentQuantiles[seq(2, 20, by = 2)] # 10 bins
  dataFrame <- do.call(data.frame,lapply(dataFrame, function(x) replace(x, is.infinite(x),NA)))
  # remove NaN and NA
  dataFrame <- dataFrame[complete.cases(dataFrame),]
  return(CFmeansForTreatVecRF(dataFrame, outVarName, treatVarName, evenQuantiles))
}


#1 LM for Lasso
CFmeansForDecileBinsLM <- function(dataFrame, outVarName, treatVarName) {
  fivePercentQuantiles <- quantile(dataFrame[[treatVarName]], prob = seq(0, 1, length = 21), type = 5, na.rm = TRUE)
  evenQuantiles <- fivePercentQuantiles[seq(2, 20, by=2)]

  # replace Inf with NA
  dataFrame <- do.call(data.frame,lapply(dataFrame, function(x) replace(x, is.infinite(x),NA)))
  # remove NaN and NA
  dataFrame <- dataFrame[complete.cases(dataFrame),]
  if ((nrow(dataFrame) == 0 )|| (mean(dataFrame$Y) == 0)){
    return (-1)
  }
  else{
    vec <- dataFrame[c(treatVarName)]
    medianValue <- median(vec[,1])
    count <- 0
    for (i in 1:nrow(dataFrame[c(treatVarName)])){
      temp <- vec[i,]
      if(temp == medianValue){
        count <- count + 1
      }
    }
    if(count < nrow(dataFrame[c(treatVarName)]) - 3){
      return(CFmeansForTreatVecLM(dataFrame, outVarName, treatVarName, evenQuantiles))
    }else{
      return(-1)
    }
  }
}

# 1 LM
CFmeansForDecileBinsLM <- function(dataFrame, outVarName, treatVarName) {
  fivePercentQuantiles <- quantile(dataFrame[[treatVarName]], prob = seq(0, 1, length = 21), type = 5, na.rm = TRUE)
  evenQuantiles <- fivePercentQuantiles[seq(2, 20, by=2)]
  return(CFmeansForTreatVecLM(dataFrame, outVarName, treatVarName, evenQuantiles))
}


CFprobsForDecileBinsRF <- function(dataFrame, outVarName, treatVarName) {

  fivePercentQuantiles <- quantile(dataFrame[[treatVarName]], prob = seq(0, 1, length = 21), type = 5)
  evenQuantiles <- fivePercentQuantiles[seq(2, 20, by=2)]
  return(CFprobsForTreatVecRF(dataFrame, outVarName, treatVarName, evenQuantiles))
}

maxContrast <- function(CFMeanVec) {

  maxCon <- 0
  index1 <- -1
  index2 <- -1

  for (i in 1:(length(CFMeanVec) - 1)) {
    for (j in (i + 1):length(CFMeanVec)) {
      if ((CFMeanVec[i] - CFMeanVec[j]) > maxCon) {
        maxCon <- CFMeanVec[i] - CFMeanVec[j]
        index1 <- i
        index2 <- j
      }
      else if ((CFMeanVec[j] - CFMeanVec[i]) > maxCon) {
        maxCon <- CFMeanVec[j] - CFMeanVec[i]
        index1 <- j
        index2 <- i
      }
    }
  }

  return(c(maxCon, index1, index2))

}

normalize <- function(x) {
# From https://stats.stackexchange.com/questions/70801/how-to-normalize-data-to-0-1-range
  x <- as.matrix(x)
  minAttr=apply(x, 2, min)
  maxAttr=apply(x, 2, max)
  x <- sweep(x, 2, minAttr, FUN="-")
  x=sweep(x, 2,  maxAttr-minAttr, "/")
  attr(x, 'normalized:min') = minAttr
  attr(x, 'normalized:max') = maxAttr
  return (x)
}

# Subject functions

goodFactor <- function(n) {

  d <- 2;
  factors <- list();
  i = 0;

  while (n > 1) {
    if (n %% d == 0) {
      i = i + 1;
      factors[[i]] <- d;
      n = n / d;
    }
    else {
      d = d + 1;
    }
  }

  return(factors);

}

goodProg <- function(x, y, z) {
  r <- 0;
  if (x > 0) {
    r <- 1;
    if (y > 0) {
      r <- 2;
      if (z > 0) {
        r <- 3;
      } else {
        r <- -3;
      }
    } else {
      r <- -2;
    }
  } else {
    r <- -1;
  }
  return(r);
}

badProg <- function(x, y, z) {
  r <- 0;
  if (x > 0) {
    r <- 1;
    if (y > 0) {
      r <- 2;
      if (z >= 0) {
        r <- 3;
      } else {
        r <- -3;
      }
    } else {
      r <- -2;
    }
  } else {
    r <- -1;
  }
  return(r);
}

# goodGCD <- function(p, q) {
#   while (q != 0) {
#     temp <- q;
#     q <- p %% q;
#     p <- temp;
#   }
#   return(p);
# }

# badGCD <- function(p, q) {
#   while (q != 0) {
#     temp <- q;
#     q <- p %% ifelse(q == 5, 4, q);
#     p <- temp;
#   }
#   return(p);
# }

compute <- function(dataframe){

  headers <- names(dataframe)
  result <- c(1, 1, 1)
  for(i in 1 : length(dataframe)){
    currentVec <- dataframe[,i]
    vec <- maxContrast(currentVec)
    result <- data.frame(result, vec)
  }
  result[,1] <- NULL
  names(result) <- headers
  return(result)
}

getTheBiggest <- function(dataframe){
  # return (names(dataframe)[order(-dataframe[1,])])
  return (dataframe[order(-dataframe[1,])])
}

ditch <- function(x){
  temp <- as.matrix(x)
  for (i in temp){
    if (i == "NaN"){
      print(i)
      i <- 0
    }
    if (i == "Inf"){
      print(i)
    }
  }
  y <- as.matrix(temp)
  print(y)
  # ifelse(is.infinite(x), 2147483647, x)
}

computeESP <- function(S_p_obs, F_p_obs, NumF, dataFrame){

  S_p <- nrow(subset(dataFrame, Y == 0))

  F_p <- nrow(subset(dataFrame, Y == 1))

  sensitivity <- log(F_p)/log(NumF)

  increase_p <- F_p/(S_p + F_p) - F_p_obs/(S_p_obs + F_p_obs)

  importance_p <- 2/((1/increase_p) + 1/(log(F_p)/log(NumF)))
  return(importance_p)
}

CFmeansForESP <- function(dataFrame, outVarName, treatVarName){

  NumF <- nrow(subset(dataFrame, Y == 1))
  # print("NumF")
  # print(NumF)

  # replace Inf with NA
  dataFrame <- do.call(data.frame,lapply(dataFrame, function(x) replace(x, is.infinite(x),NA)))
  # remove NaN and NA
  dataFrame <- dataFrame[complete.cases(dataFrame),]

  if (nrow(dataFrame) == 0){
    return(-1)
  }else{
    elastic <- data.frame(importance = c(0,0,0,0,0,0,0,0,0))

    vec <- dataFrame[c(treatVarName)]
    mu <- mean(vec[,1])
    tau <- sd(vec[,1])

    S_p_obs <- nrow(subset(dataFrame, Y == 0))
    # print("S_p_obs")
    # print(S_p_obs)
    F_p_obs <- nrow(subset(dataFrame, Y == 1))
    # print("F_p_obs")
    # print(F_p_obs)

    elastic[1,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) < mu - 3 * tau))
    elastic[2,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) >= mu - 3 * tau & eval(as.name(treatVarName)) < mu - 2 * tau))
    elastic[3,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) >= mu - 2 * tau & eval(as.name(treatVarName)) < mu - tau))
    elastic[4,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) >= mu - tau &  eval(as.name(treatVarName)) < mu))
    elastic[5,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) == mu))
    elastic[6,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) > mu & eval(as.name(treatVarName)) <= mu + tau))
    elastic[7,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) > mu + tau & eval(as.name(treatVarName)) <= mu + 2 * tau))
    elastic[8,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) > mu + 2 * tau & eval(as.name(treatVarName)) <= mu + 3 * tau))
    elastic[9,1] <- computeESP(S_p_obs, F_p_obs, NumF, subset(dataFrame, eval(as.name(treatVarName)) > mu + 3 * tau))

    elastic <- data.frame(elastic[complete.cases(elastic),])
    # print(elastic)
    maxValue <- sort(elastic[,1])[length(elastic[,1])]
    # print(maxValue)
    return (maxValue)
  }
}

is.nan.data.frame <- function(x){
  do.call(cbind, lapply(x, is.nan))
}

is.infinite.data.frame <- function(x){
  do.call(cbind, lapply(x, is.infinite))
}

#newoutput2 <- data.frame(t(newoutput))
#colnames(newoutput2) <- newoutput2[1,]

# =============================
# Start
# input: newoutput, outY
newoutput <- read.table("newoutput.txt", quote = "\"", comment.char = "")
outY <- read.table("outY.txt", quote="\"", comment.char="")
# =============================
newoutput <- data.frame(newoutput)
rownames(newoutput) <- newoutput[,1]
newoutput <- newoutput[,-1]
newoutput <- as.data.frame(t(newoutput))
Y <- data.frame(t(outY[,-1]))
names(Y) <- c("Y")
fault_binerrs_all <- data.frame(Y, newoutput)
#fault_binerrs_all <- do.call(data.frame,lapply(fault_binerrs_all, function(x) replace(x, is.infinite(x),NA)))
#fault_binerrs_all <- fault_binerrs_all[ , colSums(is.na(fault_binerrs_all)) == 0]
# trainCFoutPredRF(TestShimple_fault_binerrs_all, "Y")

for (i in seq(1,10,1)){
  CFmeanResult <- genCFmeansRF_fault_binerrs()
  # for esp
  # result <- CFmeanResult

  # for RF
  maxContrastDF <- compute(CFmeanResult)
  result <- getTheBiggest(maxContrastDF)
  if (i == 1){
    meanResult <- result[1,]
  }
  else{
    # rbind with each result, run FOR 10 TIMES
    meanResult <- rbind(meanResult, result[1,])
  }
}
# make a copy of meanResult
meanResultCopy <- meanResult
# sort by the mean Y
resultForPlot <- rbind(meanResult, colMeans(meanResult))
resultForPlot <- resultForPlot[order(resultForPlot[9,], decreasing = T)]
resultForPlot <- resultForPlot[-nrow(resultForPlot),]
# only cares about the top 20 variable in the rank
resultForPlot <- resultForPlot[,1:20]
boxplot(resultForPlot, las = 2)

resultForPlot <- rbind(resultForPlot, colMeans(resultForPlot))
write.csv(resultForPlot, file = "result.csv")
write.csv(meanResult, file = "MinCostresults/result_secMin_2_p0.75_100tests.csv")

# initialize meanReslt, only run AT THE FIRST TIME
meanResult <- result[1,]
