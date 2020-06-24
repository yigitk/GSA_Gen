genCFmeansRF_fault_binerrs <- function() {

results <- data.frame(row.names=seq(1, 10))

fault_binerrs_p1_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, p1_1=fault_binerrs_all$p1_1, p2_1=fault_binerrs_all$p2_1)
results[["p1_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_p1_1_treat_df, "Y", "p1_1")

fault_binerrs_p1_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, p1_0=fault_binerrs_all$p1_0, a0_0=fault_binerrs_all$a0_0)
results[["p1_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_p1_0_treat_df, "Y", "p1_0")

fault_binerrs_r0_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, r0_1=fault_binerrs_all$r0_1, r1_0=fault_binerrs_all$r1_0)
results[["r0_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_r0_1_treat_df, "Y", "r0_1")

fault_binerrs_r0_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, r0_2=fault_binerrs_all$r0_2, r0_1=fault_binerrs_all$r0_1, r0_0=fault_binerrs_all$r0_0)
results[["r0_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_r0_2_treat_df, "Y", "r0_2")

fault_binerrs_p1_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, p1_2=fault_binerrs_all$p1_2, p1_1=fault_binerrs_all$p1_1, p1_0=fault_binerrs_all$p1_0)
results[["p1_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_p1_2_treat_df, "Y", "p1_2")

fault_binerrs_P_32_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_32_0=fault_binerrs_all$P_32_0)
results[["P_32_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_32_0_treat_df, "Y", "P_32_0")

fault_binerrs_bits_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, bits_0=fault_binerrs_all$bits_0, value_0=fault_binerrs_all$value_0)
results[["bits_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_bits_0_treat_df, "Y", "bits_0")

fault_binerrs_P_25_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_25_1=fault_binerrs_all$P_25_1, result_0=fault_binerrs_all$result_0)
results[["P_25_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_25_1_treat_df, "Y", "P_25_1")

fault_binerrs_P_29_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_29_0=fault_binerrs_all$P_29_0)
results[["P_29_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_29_0_treat_df, "Y", "P_29_0")

fault_binerrs_P_21_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_21_1=fault_binerrs_all$P_21_1, bg_1=fault_binerrs_all$bg_1)
results[["P_21_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_21_1_treat_df, "Y", "P_21_1")

fault_binerrs_P_25_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_25_0=fault_binerrs_all$P_25_0)
results[["P_25_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_25_0_treat_df, "Y", "P_25_0")

fault_binerrs_sign_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, sign_0=fault_binerrs_all$sign_0, bits_0=fault_binerrs_all$bits_0)
results[["sign_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_sign_0_treat_df, "Y", "sign_0")

fault_binerrs_r0_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, r0_0=fault_binerrs_all$r0_0, value_2=fault_binerrs_all$value_2)
results[["r0_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_r0_0_treat_df, "Y", "r0_0")

fault_binerrs_P_29_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_29_1=fault_binerrs_all$P_29_1, bg_2=fault_binerrs_all$bg_2)
results[["P_29_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_29_1_treat_df, "Y", "P_29_1")

fault_binerrs_num_10_treat_df <- data.frame(Y=fault_binerrs_all$Y, num_10=fault_binerrs_all$num_10)
results[["num_10"]] <- CFmeansForDecileBinsRF(fault_binerrs_num_10_treat_df, "Y", "num_10")

fault_binerrs_num_11_treat_df <- data.frame(Y=fault_binerrs_all$Y, num_11=fault_binerrs_all$num_11, denominator_0=fault_binerrs_all$denominator_0)
results[["num_11"]] <- CFmeansForDecileBinsRF(fault_binerrs_num_11_treat_df, "Y", "num_11")

fault_binerrs_P_3_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_3_1=fault_binerrs_all$P_3_1, P4_1=fault_binerrs_all$P4_1, den_1=fault_binerrs_all$den_1)
results[["P_3_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_3_1_treat_df, "Y", "P_3_1")

fault_binerrs_P_3_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_3_0=fault_binerrs_all$P_3_0)
results[["P_3_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_3_0_treat_df, "Y", "P_3_0")

fault_binerrs_P_7_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_7_1=fault_binerrs_all$P_7_1, sign_0=fault_binerrs_all$sign_0)
results[["P_7_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_7_1_treat_df, "Y", "P_7_1")

fault_binerrs_P_7_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_7_0=fault_binerrs_all$P_7_0)
results[["P_7_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_7_0_treat_df, "Y", "P_7_0")

fault_binerrs_P_20_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_20_1=fault_binerrs_all$P_20_1, P6_1=fault_binerrs_all$P6_1)
results[["P_20_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_20_1_treat_df, "Y", "P_20_1")

fault_binerrs_P_20_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_20_0=fault_binerrs_all$P_20_0)
results[["P_20_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_20_0_treat_df, "Y", "P_20_0")

fault_binerrs_START_8_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, START_8_0=fault_binerrs_all$START_8_0)
results[["START_8_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_START_8_0_treat_df, "Y", "START_8_0")

fault_binerrs_START_8_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, START_8_1=fault_binerrs_all$START_8_1)
results[["START_8_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_START_8_1_treat_df, "Y", "START_8_1")

fault_binerrs_START_8_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, START_8_2=fault_binerrs_all$START_8_2, START_8_0=fault_binerrs_all$START_8_0, START_8_1=fault_binerrs_all$START_8_1)
results[["START_8_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_START_8_2_treat_df, "Y", "START_8_2")

fault_binerrs_convergent_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, convergent_0=fault_binerrs_all$convergent_0, p2_1=fault_binerrs_all$p2_1, q2_1=fault_binerrs_all$q2_1)
results[["convergent_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_convergent_0_treat_df, "Y", "convergent_0")

fault_binerrs_den_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, den_2=fault_binerrs_all$den_2)
results[["den_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_den_2_treat_df, "Y", "den_2")

fault_binerrs_P2_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P2_1=fault_binerrs_all$P2_1, num_1=fault_binerrs_all$num_1)
results[["P2_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P2_1_treat_df, "Y", "P2_1")

fault_binerrs_P_17_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_17_1=fault_binerrs_all$P_17_1, numerator_0=fault_binerrs_all$numerator_0)
results[["P_17_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_17_1_treat_df, "Y", "P_17_1")

fault_binerrs_P6_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P6_0=fault_binerrs_all$P6_0)
results[["P6_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P6_0_treat_df, "Y", "P6_0")

fault_binerrs_P_36_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_36_1=fault_binerrs_all$P_36_1, fraction_3=fault_binerrs_all$fraction_3)
results[["P_36_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_36_1_treat_df, "Y", "P_36_1")

fault_binerrs_P_17_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_17_0=fault_binerrs_all$P_17_0)
results[["P_17_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_17_0_treat_df, "Y", "P_17_0")

fault_binerrs_den_5_treat_df <- data.frame(Y=fault_binerrs_all$Y, den_5=fault_binerrs_all$den_5, denominator_0=fault_binerrs_all$denominator_0)
results[["den_5"]] <- CFmeansForDecileBinsRF(fault_binerrs_den_5_treat_df, "Y", "den_5")

fault_binerrs_den_6_treat_df <- data.frame(Y=fault_binerrs_all$Y, den_6=fault_binerrs_all$den_6)
results[["den_6"]] <- CFmeansForDecileBinsRF(fault_binerrs_den_6_treat_df, "Y", "den_6")

fault_binerrs_P_36_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_36_0=fault_binerrs_all$P_36_0)
results[["P_36_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_36_0_treat_df, "Y", "P_36_0")

fault_binerrs_den_7_treat_df <- data.frame(Y=fault_binerrs_all$Y, den_7=fault_binerrs_all$den_7, denominator_0=fault_binerrs_all$denominator_0)
results[["den_7"]] <- CFmeansForDecileBinsRF(fault_binerrs_den_7_treat_df, "Y", "den_7")

fault_binerrs_P2_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P2_0=fault_binerrs_all$P2_0)
results[["P2_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P2_0_treat_df, "Y", "P2_0")

fault_binerrs_P_13_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_13_1=fault_binerrs_all$P_13_1, p2_1=fault_binerrs_all$p2_1, overflow_0=fault_binerrs_all$overflow_0, q2_1=fault_binerrs_all$q2_1)
results[["P_13_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_13_1_treat_df, "Y", "P_13_1")

fault_binerrs_P_32_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_32_1=fault_binerrs_all$P_32_1, exponent_1=fault_binerrs_all$exponent_1)
results[["P_32_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_32_1_treat_df, "Y", "P_32_1")

fault_binerrs_den_8_treat_df <- data.frame(Y=fault_binerrs_all$Y, den_8=fault_binerrs_all$den_8)
results[["den_8"]] <- CFmeansForDecileBinsRF(fault_binerrs_den_8_treat_df, "Y", "den_8")

fault_binerrs_P_13_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_13_0=fault_binerrs_all$P_13_0)
results[["P_13_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_13_0_treat_df, "Y", "P_13_0")

fault_binerrs_P6_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P6_1=fault_binerrs_all$P6_1)
results[["P6_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P6_1_treat_df, "Y", "P6_1")

fault_binerrs_p0_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, p0_2=fault_binerrs_all$p0_2, p0_1=fault_binerrs_all$p0_1, p0_0=fault_binerrs_all$p0_0)
results[["p0_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_p0_2_treat_df, "Y", "p0_2")

fault_binerrs_p0_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, p0_1=fault_binerrs_all$p0_1, p1_0=fault_binerrs_all$p1_0)
results[["p0_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_p0_1_treat_df, "Y", "p0_1")

fault_binerrs_p0_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, p0_0=fault_binerrs_all$p0_0)
results[["p0_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_p0_0_treat_df, "Y", "p0_0")

fault_binerrs_P_10_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_10_0=fault_binerrs_all$P_10_0)
results[["P_10_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_10_0_treat_df, "Y", "P_10_0")

fault_binerrs_P_26_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_26_1=fault_binerrs_all$P_26_1, other_0=fault_binerrs_all$other_0)
results[["P_26_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_26_1_treat_df, "Y", "P_26_1")

fault_binerrs_P_26_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_26_0=fault_binerrs_all$P_26_0)
results[["P_26_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_26_0_treat_df, "Y", "P_26_0")

fault_binerrs_P_22_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_22_1=fault_binerrs_all$P_22_1, bg_1=fault_binerrs_all$bg_1)
results[["P_22_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_22_1_treat_df, "Y", "P_22_1")

fault_binerrs_P_22_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_22_0=fault_binerrs_all$P_22_0)
results[["P_22_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_22_0_treat_df, "Y", "P_22_0")

fault_binerrs_k_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, k_0=fault_binerrs_all$k_0, exponent_0=fault_binerrs_all$exponent_0)
results[["k_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_k_0_treat_df, "Y", "k_0")

fault_binerrs_q2_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, q2_2=fault_binerrs_all$q2_2, q2_0=fault_binerrs_all$q2_0, q2_1=fault_binerrs_all$q2_1)
results[["q2_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_q2_2_treat_df, "Y", "q2_2")

fault_binerrs_ret_4_treat_df <- data.frame(Y=fault_binerrs_all$Y, ret_4=fault_binerrs_all$ret_4, ret_3=fault_binerrs_all$ret_3, ret_0=fault_binerrs_all$ret_0)
results[["ret_4"]] <- CFmeansForDecileBinsRF(fault_binerrs_ret_4_treat_df, "Y", "ret_4")

fault_binerrs_ret_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, ret_2=fault_binerrs_all$ret_2)
results[["ret_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_ret_2_treat_df, "Y", "ret_2")

fault_binerrs_ret_3_treat_df <- data.frame(Y=fault_binerrs_all$Y, ret_3=fault_binerrs_all$ret_3, ret_2=fault_binerrs_all$ret_2, ret_1=fault_binerrs_all$ret_1)
results[["ret_3"]] <- CFmeansForDecileBinsRF(fault_binerrs_ret_3_treat_df, "Y", "ret_3")

fault_binerrs_ret_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, ret_0=fault_binerrs_all$ret_0)
results[["ret_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_ret_0_treat_df, "Y", "ret_0")

fault_binerrs_ret_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, ret_1=fault_binerrs_all$ret_1)
results[["ret_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_ret_1_treat_df, "Y", "ret_1")

fault_binerrs_P_2_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_2_1=fault_binerrs_all$P_2_1, P3_1=fault_binerrs_all$P3_1)
results[["P_2_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_2_1_treat_df, "Y", "P_2_1")

fault_binerrs_q2_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, q2_0=fault_binerrs_all$q2_0)
results[["q2_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_q2_0_treat_df, "Y", "q2_0")

fault_binerrs_P_2_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_2_0=fault_binerrs_all$P_2_0)
results[["P_2_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_2_0_treat_df, "Y", "P_2_0")

fault_binerrs_q2_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, q2_1=fault_binerrs_all$q2_1, q0_0=fault_binerrs_all$q0_0, q1_0=fault_binerrs_all$q1_0, a1_0=fault_binerrs_all$a1_0)
results[["q2_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_q2_1_treat_df, "Y", "q2_1")

fault_binerrs_shift_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, shift_1=fault_binerrs_all$shift_1)
results[["shift_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_shift_1_treat_df, "Y", "shift_1")

fault_binerrs_P_6_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_6_1=fault_binerrs_all$P_6_1, exponent_0=fault_binerrs_all$exponent_0)
results[["P_6_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_6_1_treat_df, "Y", "P_6_1")

fault_binerrs_P_21_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_21_0=fault_binerrs_all$P_21_0)
results[["P_21_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_21_0_treat_df, "Y", "P_21_0")

fault_binerrs_P_40_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_40_0=fault_binerrs_all$P_40_0)
results[["P_40_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_40_0_treat_df, "Y", "P_40_0")

fault_binerrs_P_6_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_6_0=fault_binerrs_all$P_6_0)
results[["P_6_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_6_0_treat_df, "Y", "P_6_0")

fault_binerrs_P_40_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_40_1=fault_binerrs_all$P_40_1, numerator_0=fault_binerrs_all$numerator_0)
results[["P_40_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_40_1_treat_df, "Y", "P_40_1")

fault_binerrs_result_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, result_2=fault_binerrs_all$result_2, result_0=fault_binerrs_all$result_0, result_1=fault_binerrs_all$result_1)
results[["result_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_result_2_treat_df, "Y", "result_2")

fault_binerrs_result_3_treat_df <- data.frame(Y=fault_binerrs_all$Y, result_3=fault_binerrs_all$result_3)
results[["result_3"]] <- CFmeansForDecileBinsRF(fault_binerrs_result_3_treat_df, "Y", "result_3")

fault_binerrs_result_4_treat_df <- data.frame(Y=fault_binerrs_all$Y, result_4=fault_binerrs_all$result_4, shift_1=fault_binerrs_all$shift_1)
results[["result_4"]] <- CFmeansForDecileBinsRF(fault_binerrs_result_4_treat_df, "Y", "result_4")

fault_binerrs_shift_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, shift_0=fault_binerrs_all$shift_0)
results[["shift_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_shift_0_treat_df, "Y", "shift_0")

fault_binerrs_result_5_treat_df <- data.frame(Y=fault_binerrs_all$Y, result_5=fault_binerrs_all$result_5, result_3=fault_binerrs_all$result_3, result_4=fault_binerrs_all$result_4)
results[["result_5"]] <- CFmeansForDecileBinsRF(fault_binerrs_result_5_treat_df, "Y", "result_5")

fault_binerrs_P_14_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_14_1=fault_binerrs_all$P_14_1, maxIterations_1=fault_binerrs_all$maxIterations_1, q2_1=fault_binerrs_all$q2_1, value_2=fault_binerrs_all$value_2, maxDenominator_0=fault_binerrs_all$maxDenominator_0, n_0=fault_binerrs_all$n_0, epsilon_1=fault_binerrs_all$epsilon_1, convergent_0=fault_binerrs_all$convergent_0)
results[["P_14_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_14_1_treat_df, "Y", "P_14_1")

fault_binerrs_P5_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P5_1=fault_binerrs_all$P5_1, m_4=fault_binerrs_all$m_4)
results[["P5_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P5_1_treat_df, "Y", "P5_1")

fault_binerrs_P_18_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_18_0=fault_binerrs_all$P_18_0)
results[["P_18_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_18_0_treat_df, "Y", "P_18_0")

fault_binerrs_result_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, result_0=fault_binerrs_all$result_0)
results[["result_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_result_0_treat_df, "Y", "result_0")

fault_binerrs_P_37_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_37_0=fault_binerrs_all$P_37_0)
results[["P_37_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_37_0_treat_df, "Y", "P_37_0")

fault_binerrs_P5_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P5_0=fault_binerrs_all$P5_0, m_4=fault_binerrs_all$m_4)
results[["P5_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P5_0_treat_df, "Y", "P5_0")

fault_binerrs_result_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, result_1=fault_binerrs_all$result_1, shift_0=fault_binerrs_all$shift_0)
results[["result_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_result_1_treat_df, "Y", "result_1")

fault_binerrs_P_37_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_37_1=fault_binerrs_all$P_37_1, fraction_3=fault_binerrs_all$fraction_3)
results[["P_37_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_37_1_treat_df, "Y", "P_37_1")

fault_binerrs_P_10_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_10_1=fault_binerrs_all$P_10_1, overflow_0=fault_binerrs_all$overflow_0, a0_0=fault_binerrs_all$a0_0)
results[["P_10_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_10_1_treat_df, "Y", "P_10_1")

fault_binerrs_P1_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P1_1=fault_binerrs_all$P1_1, den_0=fault_binerrs_all$den_0)
results[["P1_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P1_1_treat_df, "Y", "P1_1")

fault_binerrs_P_14_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_14_0=fault_binerrs_all$P_14_0)
results[["P_14_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_14_0_treat_df, "Y", "P_14_0")

fault_binerrs_P_33_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_33_0=fault_binerrs_all$P_33_0)
results[["P_33_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_33_0_treat_df, "Y", "P_33_0")

fault_binerrs_P1_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P1_0=fault_binerrs_all$P1_0)
results[["P1_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P1_0_treat_df, "Y", "P1_0")

fault_binerrs_P_33_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_33_1=fault_binerrs_all$P_33_1, exponent_2=fault_binerrs_all$exponent_2)
results[["P_33_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_33_1_treat_df, "Y", "P_33_1")

fault_binerrs_exponent_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, exponent_0=fault_binerrs_all$exponent_0, bits_0=fault_binerrs_all$bits_0)
results[["exponent_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_exponent_0_treat_df, "Y", "exponent_0")

fault_binerrs_P5_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, P5_2=fault_binerrs_all$P5_2, P5_1=fault_binerrs_all$P5_1, P5_0=fault_binerrs_all$P5_0)
results[["P5_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_P5_2_treat_df, "Y", "P5_2")

fault_binerrs_P_18_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_18_1=fault_binerrs_all$P_18_1, fraction_0=fault_binerrs_all$fraction_0)
results[["P_18_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_18_1_treat_df, "Y", "P_18_1")

fault_binerrs_num_9_treat_df <- data.frame(Y=fault_binerrs_all$Y, num_9=fault_binerrs_all$num_9, denominator_0=fault_binerrs_all$denominator_0)
results[["num_9"]] <- CFmeansForDecileBinsRF(fault_binerrs_num_9_treat_df, "Y", "num_9")

fault_binerrs_P_30_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_30_1=fault_binerrs_all$P_30_1, fraction_2=fault_binerrs_all$fraction_2)
results[["P_30_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_30_1_treat_df, "Y", "P_30_1")

fault_binerrs_P_30_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_30_0=fault_binerrs_all$P_30_0)
results[["P_30_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_30_0_treat_df, "Y", "P_30_0")

fault_binerrs_a1_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, a1_0=fault_binerrs_all$a1_0, r1_0=fault_binerrs_all$r1_0)
results[["a1_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_a1_0_treat_df, "Y", "a1_0")

fault_binerrs_P_27_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_27_0=fault_binerrs_all$P_27_0)
results[["P_27_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_27_0_treat_df, "Y", "P_27_0")

fault_binerrs_P_27_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_27_1=fault_binerrs_all$P_27_1, other_0=fault_binerrs_all$other_0)
results[["P_27_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_27_1_treat_df, "Y", "P_27_1")

fault_binerrs_P_23_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_23_0=fault_binerrs_all$P_23_0)
results[["P_23_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_23_0_treat_df, "Y", "P_23_0")

fault_binerrs_P_23_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_23_1=fault_binerrs_all$P_23_1, fraction_1=fault_binerrs_all$fraction_1)
results[["P_23_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_23_1_treat_df, "Y", "P_23_1")

fault_binerrs_n_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, n_0=fault_binerrs_all$n_0)
results[["n_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_n_0_treat_df, "Y", "n_0")

fault_binerrs_P_1_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_1_0=fault_binerrs_all$P_1_0)
results[["P_1_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_1_0_treat_df, "Y", "P_1_0")

fault_binerrs_P_5_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_5_0=fault_binerrs_all$P_5_0)
results[["P_5_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_5_0_treat_df, "Y", "P_5_0")

fault_binerrs_q1_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, q1_0=fault_binerrs_all$q1_0)
results[["q1_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_q1_0_treat_df, "Y", "q1_0")

fault_binerrs_q1_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, q1_1=fault_binerrs_all$q1_1, q2_1=fault_binerrs_all$q2_1)
results[["q1_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_q1_1_treat_df, "Y", "q1_1")

fault_binerrs_P_1_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_1_1=fault_binerrs_all$P_1_1, P2_1=fault_binerrs_all$P2_1, num_1=fault_binerrs_all$num_1)
results[["P_1_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_1_1_treat_df, "Y", "P_1_1")

fault_binerrs_q1_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, q1_2=fault_binerrs_all$q1_2, q1_0=fault_binerrs_all$q1_0, q1_1=fault_binerrs_all$q1_1)
results[["q1_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_q1_2_treat_df, "Y", "q1_2")

fault_binerrs_P_9_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_9_0=fault_binerrs_all$P_9_0)
results[["P_9_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_9_0_treat_df, "Y", "P_9_0")

fault_binerrs_START_12_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, START_12_1=fault_binerrs_all$START_12_1)
results[["START_12_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_START_12_1_treat_df, "Y", "START_12_1")

fault_binerrs_START_12_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, START_12_2=fault_binerrs_all$START_12_2, START_12_0=fault_binerrs_all$START_12_0, START_12_1=fault_binerrs_all$START_12_1)
results[["START_12_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_START_12_2_treat_df, "Y", "START_12_2")

fault_binerrs_P_5_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_5_1=fault_binerrs_all$P_5_1, value_0=fault_binerrs_all$value_0)
results[["P_5_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_5_1_treat_df, "Y", "P_5_1")

fault_binerrs_P_9_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_9_1=fault_binerrs_all$P_9_1, k_0=fault_binerrs_all$k_0)
results[["P_9_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_9_1_treat_df, "Y", "P_9_1")

fault_binerrs_P4_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P4_0=fault_binerrs_all$P4_0)
results[["P4_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P4_0_treat_df, "Y", "P4_0")

fault_binerrs_P_15_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_15_1=fault_binerrs_all$P_15_1, maxIterations_1=fault_binerrs_all$maxIterations_1, n_0=fault_binerrs_all$n_0)
results[["P_15_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_15_1_treat_df, "Y", "P_15_1")

fault_binerrs_P_38_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_38_1=fault_binerrs_all$P_38_1)
results[["P_38_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_38_1_treat_df, "Y", "P_38_1")

fault_binerrs_P_15_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_15_0=fault_binerrs_all$P_15_0)
results[["P_15_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_15_0_treat_df, "Y", "P_15_0")

fault_binerrs_P4_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P4_1=fault_binerrs_all$P4_1, den_1=fault_binerrs_all$den_1)
results[["P4_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P4_1_treat_df, "Y", "P4_1")

fault_binerrs_P_38_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_38_0=fault_binerrs_all$P_38_0)
results[["P_38_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_38_0_treat_df, "Y", "P_38_0")

fault_binerrs_P_11_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_11_1=fault_binerrs_all$P_11_1, value_2=fault_binerrs_all$value_2, a0_0=fault_binerrs_all$a0_0, epsilon_1=fault_binerrs_all$epsilon_1)
results[["P_11_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_11_1_treat_df, "Y", "P_11_1")

fault_binerrs_P_34_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_34_1=fault_binerrs_all$P_34_1)
results[["P_34_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_34_1_treat_df, "Y", "P_34_1")

fault_binerrs_P_11_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_11_0=fault_binerrs_all$P_11_0)
results[["P_11_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_11_0_treat_df, "Y", "P_11_0")

fault_binerrs_START_12_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, START_12_0=fault_binerrs_all$START_12_0)
results[["START_12_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_START_12_0_treat_df, "Y", "START_12_0")

fault_binerrs_P_34_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_34_0=fault_binerrs_all$P_34_0)
results[["P_34_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_34_0_treat_df, "Y", "P_34_0")

fault_binerrs_P_19_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_19_1=fault_binerrs_all$P_19_1, fraction_0=fault_binerrs_all$fraction_0)
results[["P_19_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_19_1_treat_df, "Y", "P_19_1")

fault_binerrs_P_19_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_19_0=fault_binerrs_all$P_19_0)
results[["P_19_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_19_0_treat_df, "Y", "P_19_0")

fault_binerrs_p2_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, p2_0=fault_binerrs_all$p2_0)
results[["p2_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_p2_0_treat_df, "Y", "p2_0")

fault_binerrs_r1_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, r1_0=fault_binerrs_all$r1_0, r0_0=fault_binerrs_all$r0_0, a0_0=fault_binerrs_all$a0_0)
results[["r1_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_r1_0_treat_df, "Y", "r1_0")

fault_binerrs_p2_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, p2_2=fault_binerrs_all$p2_2, p2_0=fault_binerrs_all$p2_0, p2_1=fault_binerrs_all$p2_1)
results[["p2_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_p2_2_treat_df, "Y", "p2_2")

fault_binerrs_overflow_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, overflow_0=fault_binerrs_all$overflow_0)
results[["overflow_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_overflow_0_treat_df, "Y", "overflow_0")

fault_binerrs_p2_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, p2_1=fault_binerrs_all$p2_1, p1_0=fault_binerrs_all$p1_0, p0_0=fault_binerrs_all$p0_0, a1_0=fault_binerrs_all$a1_0)
results[["p2_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_p2_1_treat_df, "Y", "p2_1")

fault_binerrs_P_31_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_31_0=fault_binerrs_all$P_31_0)
results[["P_31_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_31_0_treat_df, "Y", "P_31_0")

fault_binerrs_P_31_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_31_1=fault_binerrs_all$P_31_1)
results[["P_31_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_31_1_treat_df, "Y", "P_31_1")

fault_binerrs_a0_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, a0_0=fault_binerrs_all$a0_0, r0_0=fault_binerrs_all$r0_0)
results[["a0_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_a0_0_treat_df, "Y", "a0_0")

fault_binerrs_a0_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, a0_1=fault_binerrs_all$a0_1, a1_0=fault_binerrs_all$a1_0)
results[["a0_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_a0_1_treat_df, "Y", "a0_1")

fault_binerrs_a0_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, a0_2=fault_binerrs_all$a0_2, a0_0=fault_binerrs_all$a0_0, a0_1=fault_binerrs_all$a0_1)
results[["a0_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_a0_2_treat_df, "Y", "a0_2")

fault_binerrs_den_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, den_1=fault_binerrs_all$den_1)
results[["den_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_den_1_treat_df, "Y", "den_1")

fault_binerrs_P_28_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_28_0=fault_binerrs_all$P_28_0)
results[["P_28_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_28_0_treat_df, "Y", "P_28_0")

fault_binerrs_P_28_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_28_1=fault_binerrs_all$P_28_1, result_3=fault_binerrs_all$result_3)
results[["P_28_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_28_1_treat_df, "Y", "P_28_1")

fault_binerrs_P_24_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_24_1=fault_binerrs_all$P_24_1)
results[["P_24_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_24_1_treat_df, "Y", "P_24_1")

fault_binerrs_P_24_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_24_0=fault_binerrs_all$P_24_0)
results[["P_24_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_24_0_treat_df, "Y", "P_24_0")

fault_binerrs_m_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, m_1=fault_binerrs_all$m_1)
results[["m_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_m_1_treat_df, "Y", "m_1")

fault_binerrs_m_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, m_0=fault_binerrs_all$m_0, bits_0=fault_binerrs_all$bits_0)
results[["m_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_m_0_treat_df, "Y", "m_0")

fault_binerrs_P_0_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_0_1=fault_binerrs_all$P_0_1, P1_1=fault_binerrs_all$P1_1, den_0=fault_binerrs_all$den_0)
results[["P_0_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_0_1_treat_df, "Y", "P_0_1")

fault_binerrs_m_3_treat_df <- data.frame(Y=fault_binerrs_all$Y, m_3=fault_binerrs_all$m_3, m_2=fault_binerrs_all$m_2)
results[["m_3"]] <- CFmeansForDecileBinsRF(fault_binerrs_m_3_treat_df, "Y", "m_3")

fault_binerrs_stop_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, stop_1=fault_binerrs_all$stop_1)
results[["stop_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_stop_1_treat_df, "Y", "stop_1")

fault_binerrs_P_0_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_0_0=fault_binerrs_all$P_0_0)
results[["P_0_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_0_0_treat_df, "Y", "P_0_0")

fault_binerrs_m_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, m_2=fault_binerrs_all$m_2, m_1=fault_binerrs_all$m_1, m_0=fault_binerrs_all$m_0)
results[["m_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_m_2_treat_df, "Y", "m_2")

fault_binerrs_stop_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, stop_2=fault_binerrs_all$stop_2, stop_1=fault_binerrs_all$stop_1, stop_0=fault_binerrs_all$stop_0)
results[["stop_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_stop_2_treat_df, "Y", "stop_2")

fault_binerrs_m_5_treat_df <- data.frame(Y=fault_binerrs_all$Y, m_5=fault_binerrs_all$m_5, m_4=fault_binerrs_all$m_4)
results[["m_5"]] <- CFmeansForDecileBinsRF(fault_binerrs_m_5_treat_df, "Y", "m_5")

fault_binerrs_m_4_treat_df <- data.frame(Y=fault_binerrs_all$Y, m_4=fault_binerrs_all$m_4, m_3=fault_binerrs_all$m_3, m_2=fault_binerrs_all$m_2)
results[["m_4"]] <- CFmeansForDecileBinsRF(fault_binerrs_m_4_treat_df, "Y", "m_4")

fault_binerrs_P_4_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_4_1=fault_binerrs_all$P_4_1, value_0=fault_binerrs_all$value_0)
results[["P_4_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_4_1_treat_df, "Y", "P_4_1")

fault_binerrs_q0_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, q0_0=fault_binerrs_all$q0_0)
results[["q0_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_q0_0_treat_df, "Y", "q0_0")

fault_binerrs_P_4_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_4_0=fault_binerrs_all$P_4_0)
results[["P_4_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_4_0_treat_df, "Y", "P_4_0")

fault_binerrs_m_6_treat_df <- data.frame(Y=fault_binerrs_all$Y, m_6=fault_binerrs_all$m_6, m_5=fault_binerrs_all$m_5, m_4=fault_binerrs_all$m_4)
results[["m_6"]] <- CFmeansForDecileBinsRF(fault_binerrs_m_6_treat_df, "Y", "m_6")

fault_binerrs_q0_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, q0_1=fault_binerrs_all$q0_1, q1_0=fault_binerrs_all$q1_0)
results[["q0_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_q0_1_treat_df, "Y", "q0_1")

fault_binerrs_q0_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, q0_2=fault_binerrs_all$q0_2, q0_0=fault_binerrs_all$q0_0, q0_1=fault_binerrs_all$q0_1)
results[["q0_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_q0_2_treat_df, "Y", "q0_2")

fault_binerrs_stop_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, stop_0=fault_binerrs_all$stop_0)
results[["stop_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_stop_0_treat_df, "Y", "stop_0")

fault_binerrs_P_8_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_8_1=fault_binerrs_all$P_8_1, P5_1=fault_binerrs_all$P5_1, m_4=fault_binerrs_all$m_4)
results[["P_8_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_8_1_treat_df, "Y", "P_8_1")

fault_binerrs_P_8_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_8_0=fault_binerrs_all$P_8_0, P5_0=fault_binerrs_all$P5_0, m_4=fault_binerrs_all$m_4)
results[["P_8_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_8_0_treat_df, "Y", "P_8_0")

fault_binerrs_P_8_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_8_2=fault_binerrs_all$P_8_2, P_8_1=fault_binerrs_all$P_8_1, P_8_0=fault_binerrs_all$P_8_0)
results[["P_8_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_8_2_treat_df, "Y", "P_8_2")

fault_binerrs_P3_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P3_1=fault_binerrs_all$P3_1)
results[["P3_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P3_1_treat_df, "Y", "P3_1")

fault_binerrs_P_16_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_16_0=fault_binerrs_all$P_16_0)
results[["P_16_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_16_0_treat_df, "Y", "P_16_0")

fault_binerrs_P_39_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_39_0=fault_binerrs_all$P_39_0)
results[["P_39_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_39_0_treat_df, "Y", "P_39_0")

fault_binerrs_P3_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P3_0=fault_binerrs_all$P3_0)
results[["P3_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P3_0_treat_df, "Y", "P3_0")

fault_binerrs_P_39_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_39_1=fault_binerrs_all$P_39_1, denominator_0=fault_binerrs_all$denominator_0)
results[["P_39_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_39_1_treat_df, "Y", "P_39_1")

fault_binerrs_P_16_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_16_1=fault_binerrs_all$P_16_1, q2_2=fault_binerrs_all$q2_2, maxDenominator_0=fault_binerrs_all$maxDenominator_0)
results[["P_16_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_16_1_treat_df, "Y", "P_16_1")

fault_binerrs_P_12_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_12_0=fault_binerrs_all$P_12_0)
results[["P_12_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_12_0_treat_df, "Y", "P_12_0")

fault_binerrs_P_12_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_12_2=fault_binerrs_all$P_12_2, P_12_0=fault_binerrs_all$P_12_0, P_12_1=fault_binerrs_all$P_12_1)
results[["P_12_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_12_2_treat_df, "Y", "P_12_2")

fault_binerrs_P_12_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, P_12_1=fault_binerrs_all$P_12_1, stop_0=fault_binerrs_all$stop_0)
results[["P_12_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_P_12_1_treat_df, "Y", "P_12_1")

fault_binerrs_num_8_treat_df <- data.frame(Y=fault_binerrs_all$Y, num_8=fault_binerrs_all$num_8)
results[["num_8"]] <- CFmeansForDecileBinsRF(fault_binerrs_num_8_treat_df, "Y", "num_8")

fault_binerrs_num_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, num_2=fault_binerrs_all$num_2)
results[["num_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_num_2_treat_df, "Y", "num_2")

fault_binerrs_num_3_treat_df <- data.frame(Y=fault_binerrs_all$Y, num_3=fault_binerrs_all$num_3)
results[["num_3"]] <- CFmeansForDecileBinsRF(fault_binerrs_num_3_treat_df, "Y", "num_3")

return(results)

}
