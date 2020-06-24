genCFmeansRF_fault_binerrs <- function() {

results <- data.frame(row.names=seq(1, 10))

fault_binerrs_i_12_treat_df <- data.frame(Y=fault_binerrs_all$Y, i_12=fault_binerrs_all$i_12, b_1=fault_binerrs_all$b_1)
results[["i_12"]] <- CFmeansForDecileBinsRF(fault_binerrs_i_12_treat_df, "Y", "i_12")

fault_binerrs_i_14_treat_df <- data.frame(Y=fault_binerrs_all$Y, i_14=fault_binerrs_all$i_14)
results[["i_14"]] <- CFmeansForDecileBinsRF(fault_binerrs_i_14_treat_df, "Y", "i_14")

fault_binerrs_i_13_treat_df <- data.frame(Y=fault_binerrs_all$Y, i_13=fault_binerrs_all$i_13, a_1=fault_binerrs_all$a_1, b_4=fault_binerrs_all$b_4)
results[["i_13"]] <- CFmeansForDecileBinsRF(fault_binerrs_i_13_treat_df, "Y", "i_13")

fault_binerrs_i_16_treat_df <- data.frame(Y=fault_binerrs_all$Y, i_16=fault_binerrs_all$i_16, i_15=fault_binerrs_all$i_15)
results[["i_16"]] <- CFmeansForDecileBinsRF(fault_binerrs_i_16_treat_df, "Y", "i_16")

fault_binerrs_i_15_treat_df <- data.frame(Y=fault_binerrs_all$Y, i_15=fault_binerrs_all$i_15, i_14=fault_binerrs_all$i_14, i_16=fault_binerrs_all$i_16)
results[["i_15"]] <- CFmeansForDecileBinsRF(fault_binerrs_i_15_treat_df, "Y", "i_15")

fault_binerrs_b_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, b_2=fault_binerrs_all$b_2, b_1=fault_binerrs_all$b_1, dual_0=fault_binerrs_all$dual_0)
results[["b_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_b_2_treat_df, "Y", "b_2")

fault_binerrs_b_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, b_1=fault_binerrs_all$b_1, b_0=fault_binerrs_all$b_0, b_2=fault_binerrs_all$b_2)
results[["b_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_b_1_treat_df, "Y", "b_1")

fault_binerrs_b_4_treat_df <- data.frame(Y=fault_binerrs_all$Y, b_4=fault_binerrs_all$b_4, b_3=fault_binerrs_all$b_3, b_5=fault_binerrs_all$b_5)
results[["b_4"]] <- CFmeansForDecileBinsRF(fault_binerrs_b_4_treat_df, "Y", "b_4")

fault_binerrs_bit_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, bit_2=fault_binerrs_all$bit_2, bit_1=fault_binerrs_all$bit_1)
results[["bit_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_bit_2_treat_df, "Y", "bit_2")

fault_binerrs_b_5_treat_df <- data.frame(Y=fault_binerrs_all$Y, b_5=fault_binerrs_all$b_5, b_4=fault_binerrs_all$b_4, dual_1=fault_binerrs_all$dual_1)
results[["b_5"]] <- CFmeansForDecileBinsRF(fault_binerrs_b_5_treat_df, "Y", "b_5")

fault_binerrs_j_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, j_0=fault_binerrs_all$j_0, b_1=fault_binerrs_all$b_1, dual_0=fault_binerrs_all$dual_0)
results[["j_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_j_0_treat_df, "Y", "j_0")

fault_binerrs_tmp_imag_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, tmp_imag_0=fault_binerrs_all$tmp_imag_0, w_imag_0=fault_binerrs_all$w_imag_0, s_0=fault_binerrs_all$s_0, s2_0=fault_binerrs_all$s2_0, w_real_0=fault_binerrs_all$w_real_0)
results[["tmp_imag_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_tmp_imag_0_treat_df, "Y", "tmp_imag_0")

fault_binerrs_j_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, j_2=fault_binerrs_all$j_2)
results[["j_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_j_2_treat_df, "Y", "j_2")

fault_binerrs_tmp_imag_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, tmp_imag_1=fault_binerrs_all$tmp_imag_1, ii_0=fault_binerrs_all$ii_0)
results[["tmp_imag_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_tmp_imag_1_treat_df, "Y", "tmp_imag_1")

fault_binerrs_j_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, j_1=fault_binerrs_all$j_1, a_1=fault_binerrs_all$a_1, b_4=fault_binerrs_all$b_4, dual_1=fault_binerrs_all$dual_1)
results[["j_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_j_1_treat_df, "Y", "j_1")

fault_binerrs_j_3_treat_df <- data.frame(Y=fault_binerrs_all$Y, j_3=fault_binerrs_all$j_3, k_3=fault_binerrs_all$k_3)
results[["j_3"]] <- CFmeansForDecileBinsRF(fault_binerrs_j_3_treat_df, "Y", "j_3")

fault_binerrs_j_6_treat_df <- data.frame(Y=fault_binerrs_all$Y, j_6=fault_binerrs_all$j_6, j_5=fault_binerrs_all$j_5, j_2=fault_binerrs_all$j_2)
results[["j_6"]] <- CFmeansForDecileBinsRF(fault_binerrs_j_6_treat_df, "Y", "j_6")

fault_binerrs_wd_imag_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, wd_imag_1=fault_binerrs_all$wd_imag_1, w_imag_1=fault_binerrs_all$w_imag_1, z1_imag_0=fault_binerrs_all$z1_imag_0, w_real_1=fault_binerrs_all$w_real_1, z1_real_0=fault_binerrs_all$z1_real_0)
results[["wd_imag_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_wd_imag_1_treat_df, "Y", "wd_imag_1")

fault_binerrs_j_5_treat_df <- data.frame(Y=fault_binerrs_all$Y, j_5=fault_binerrs_all$j_5, k_6=fault_binerrs_all$k_6)
results[["j_5"]] <- CFmeansForDecileBinsRF(fault_binerrs_j_5_treat_df, "Y", "j_5")

fault_binerrs_wd_imag_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, wd_imag_0=fault_binerrs_all$wd_imag_0, j_0=fault_binerrs_all$j_0)
results[["wd_imag_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_wd_imag_0_treat_df, "Y", "wd_imag_0")

fault_binerrs_n_6_treat_df <- data.frame(Y=fault_binerrs_all$Y, n_6=fault_binerrs_all$n_6)
results[["n_6"]] <- CFmeansForDecileBinsRF(fault_binerrs_n_6_treat_df, "Y", "n_6")

fault_binerrs_t_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, t_0=fault_binerrs_all$t_0, theta_0=fault_binerrs_all$theta_0)
results[["t_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_t_0_treat_df, "Y", "t_0")

fault_binerrs_n_5_treat_df <- data.frame(Y=fault_binerrs_all$Y, n_5=fault_binerrs_all$n_5, n_4=fault_binerrs_all$n_4)
results[["n_5"]] <- CFmeansForDecileBinsRF(fault_binerrs_n_5_treat_df, "Y", "n_5")

fault_binerrs_s2_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, s2_0=fault_binerrs_all$s2_0, t_0=fault_binerrs_all$t_0)
results[["s2_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_s2_0_treat_df, "Y", "s2_0")

fault_binerrs_n_8_treat_df <- data.frame(Y=fault_binerrs_all$Y, n_8=fault_binerrs_all$n_8, n_7=fault_binerrs_all$n_7)
results[["n_8"]] <- CFmeansForDecileBinsRF(fault_binerrs_n_8_treat_df, "Y", "n_8")

fault_binerrs_n_7_treat_df <- data.frame(Y=fault_binerrs_all$Y, n_7=fault_binerrs_all$n_7, n_6=fault_binerrs_all$n_6)
results[["n_7"]] <- CFmeansForDecileBinsRF(fault_binerrs_n_7_treat_df, "Y", "n_7")

fault_binerrs_n_9_treat_df <- data.frame(Y=fault_binerrs_all$Y, n_9=fault_binerrs_all$n_9)
results[["n_9"]] <- CFmeansForDecileBinsRF(fault_binerrs_n_9_treat_df, "Y", "n_9")

fault_binerrs_w_imag_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, w_imag_1=fault_binerrs_all$w_imag_1, tmp_imag_0=fault_binerrs_all$tmp_imag_0)
results[["w_imag_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_w_imag_1_treat_df, "Y", "w_imag_1")

fault_binerrs_w_imag_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, w_imag_0=fault_binerrs_all$w_imag_0)
results[["w_imag_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_w_imag_0_treat_df, "Y", "w_imag_0")

fault_binerrs_jj_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, jj_0=fault_binerrs_all$jj_0, j_2=fault_binerrs_all$j_2)
results[["jj_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_jj_0_treat_df, "Y", "jj_0")

fault_binerrs_logn_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, logn_0=fault_binerrs_all$logn_0, n_6=fault_binerrs_all$n_6)
results[["logn_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_logn_0_treat_df, "Y", "logn_0")

fault_binerrs_z1_imag_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, z1_imag_0=fault_binerrs_all$z1_imag_0, j_1=fault_binerrs_all$j_1)
results[["z1_imag_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_z1_imag_0_treat_df, "Y", "z1_imag_0")

fault_binerrs_nm1_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, nm1_0=fault_binerrs_all$nm1_0, n_9=fault_binerrs_all$n_9)
results[["nm1_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_nm1_0_treat_df, "Y", "nm1_0")

fault_binerrs_logn_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, logn_1=fault_binerrs_all$logn_1, logn_0=fault_binerrs_all$logn_0)
results[["logn_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_logn_1_treat_df, "Y", "logn_1")

fault_binerrs_nm1_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, nm1_1=fault_binerrs_all$nm1_1, nm1_0=fault_binerrs_all$nm1_0)
results[["nm1_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_nm1_1_treat_df, "Y", "nm1_1")

fault_binerrs_log_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, log_0=fault_binerrs_all$log_0)
results[["log_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_log_0_treat_df, "Y", "log_0")

fault_binerrs_a_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, a_1=fault_binerrs_all$a_1, a_0=fault_binerrs_all$a_0, a_2=fault_binerrs_all$a_2)
results[["a_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_a_1_treat_df, "Y", "a_1")

fault_binerrs_log_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, log_1=fault_binerrs_all$log_1, log_0=fault_binerrs_all$log_0)
results[["log_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_log_1_treat_df, "Y", "log_1")

fault_binerrs_tmp_real_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, tmp_real_0=fault_binerrs_all$tmp_real_0, s_0=fault_binerrs_all$s_0, w_imag_0=fault_binerrs_all$w_imag_0, s2_0=fault_binerrs_all$s2_0, w_real_0=fault_binerrs_all$w_real_0)
results[["tmp_real_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_tmp_real_0_treat_df, "Y", "tmp_real_0")

fault_binerrs_a_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, a_2=fault_binerrs_all$a_2, a_1=fault_binerrs_all$a_1)
results[["a_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_a_2_treat_df, "Y", "a_2")

fault_binerrs_tmp_real_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, tmp_real_1=fault_binerrs_all$tmp_real_1, ii_0=fault_binerrs_all$ii_0)
results[["tmp_real_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_tmp_real_1_treat_df, "Y", "tmp_real_1")

fault_binerrs_k_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, k_1=fault_binerrs_all$k_1, k_0=fault_binerrs_all$k_0, k_2=fault_binerrs_all$k_2)
results[["k_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_k_1_treat_df, "Y", "k_1")

fault_binerrs_k_3_treat_df <- data.frame(Y=fault_binerrs_all$Y, k_3=fault_binerrs_all$k_3, n_9=fault_binerrs_all$n_9)
results[["k_3"]] <- CFmeansForDecileBinsRF(fault_binerrs_k_3_treat_df, "Y", "k_3")

fault_binerrs_k_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, k_2=fault_binerrs_all$k_2, k_1=fault_binerrs_all$k_1)
results[["k_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_k_2_treat_df, "Y", "k_2")

fault_binerrs_theta_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, theta_0=fault_binerrs_all$theta_0, direction_0=fault_binerrs_all$direction_0, dual_0=fault_binerrs_all$dual_0)
results[["theta_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_theta_0_treat_df, "Y", "theta_0")

fault_binerrs_ii_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, ii_0=fault_binerrs_all$ii_0, i_15=fault_binerrs_all$i_15)
results[["ii_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_ii_0_treat_df, "Y", "ii_0")

fault_binerrs_k_4_treat_df <- data.frame(Y=fault_binerrs_all$Y, k_4=fault_binerrs_all$k_4)
results[["k_4"]] <- CFmeansForDecileBinsRF(fault_binerrs_k_4_treat_df, "Y", "k_4")

fault_binerrs_k_6_treat_df <- data.frame(Y=fault_binerrs_all$Y, k_6=fault_binerrs_all$k_6, k_5=fault_binerrs_all$k_5, k_3=fault_binerrs_all$k_3)
results[["k_6"]] <- CFmeansForDecileBinsRF(fault_binerrs_k_6_treat_df, "Y", "k_6")

fault_binerrs_s_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, s_0=fault_binerrs_all$s_0, theta_0=fault_binerrs_all$theta_0)
results[["s_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_s_0_treat_df, "Y", "s_0")

fault_binerrs_bit_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, bit_1=fault_binerrs_all$bit_1, bit_2=fault_binerrs_all$bit_2, bit_0=fault_binerrs_all$bit_0)
results[["bit_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_bit_1_treat_df, "Y", "bit_1")

fault_binerrs_dual_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, dual_2=fault_binerrs_all$dual_2, dual_1=fault_binerrs_all$dual_1)
results[["dual_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_dual_2_treat_df, "Y", "dual_2")

fault_binerrs_dual_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, dual_1=fault_binerrs_all$dual_1, dual_0=fault_binerrs_all$dual_0)
results[["dual_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_dual_1_treat_df, "Y", "dual_1")

fault_binerrs_w_real_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, w_real_0=fault_binerrs_all$w_real_0)
results[["w_real_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_w_real_0_treat_df, "Y", "w_real_0")

fault_binerrs_wd_real_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, wd_real_1=fault_binerrs_all$wd_real_1, w_imag_1=fault_binerrs_all$w_imag_1, z1_imag_0=fault_binerrs_all$z1_imag_0, w_real_1=fault_binerrs_all$w_real_1, z1_real_0=fault_binerrs_all$z1_real_0)
results[["wd_real_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_wd_real_1_treat_df, "Y", "wd_real_1")

fault_binerrs_w_real_2_treat_df <- data.frame(Y=fault_binerrs_all$Y, w_real_2=fault_binerrs_all$w_real_2, w_real_0=fault_binerrs_all$w_real_0, w_real_1=fault_binerrs_all$w_real_1)
results[["w_real_2"]] <- CFmeansForDecileBinsRF(fault_binerrs_w_real_2_treat_df, "Y", "w_real_2")

fault_binerrs_w_real_1_treat_df <- data.frame(Y=fault_binerrs_all$Y, w_real_1=fault_binerrs_all$w_real_1, tmp_real_0=fault_binerrs_all$tmp_real_0)
results[["w_real_1"]] <- CFmeansForDecileBinsRF(fault_binerrs_w_real_1_treat_df, "Y", "w_real_1")

fault_binerrs_wd_real_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, wd_real_0=fault_binerrs_all$wd_real_0, j_0=fault_binerrs_all$j_0)
results[["wd_real_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_wd_real_0_treat_df, "Y", "wd_real_0")

fault_binerrs_z1_real_0_treat_df <- data.frame(Y=fault_binerrs_all$Y, z1_real_0=fault_binerrs_all$z1_real_0, j_1=fault_binerrs_all$j_1)
results[["z1_real_0"]] <- CFmeansForDecileBinsRF(fault_binerrs_z1_real_0_treat_df, "Y", "z1_real_0")

return(results)

}
