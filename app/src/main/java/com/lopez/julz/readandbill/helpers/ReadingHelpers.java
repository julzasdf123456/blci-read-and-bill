package com.lopez.julz.readandbill.helpers;

import android.util.Log;

import com.lopez.julz.readandbill.dao.Bills;
import com.lopez.julz.readandbill.dao.DownloadedPreviousReadings;
import com.lopez.julz.readandbill.dao.Rates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ReadingHelpers {
    public static String getKwhUsed(DownloadedPreviousReadings dprPrev, Double current) {
        try {
            String kwUsed = dprPrev.getKwhUsed() != null ? (dprPrev.getKwhUsed().length() > 0 ? dprPrev.getKwhUsed() : "0") : "0";
            Double prev = 0.0;
            if (dprPrev.getChangeMeterStartKwh() != null) {
                prev = Double.valueOf(dprPrev.getChangeMeterStartKwh());
            } else {
                prev = Double.valueOf(kwUsed);
            }
            return (current - prev) + "";
        } catch (Exception e) {
            Log.e("ERR_GET_KWH", e.getMessage());
            return "";
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String generateBillNumber(String areaCode) {
        try {
            String time = new Date().getTime() + "";
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getServiceFromFromServicePeriod(String servicePeriod) {
        try {
            servicePeriod = servicePeriod.substring(0, 6) + "-24";
            Calendar c = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(sdf.parse(servicePeriod));
            c.add(Calendar.MONTH, -1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            Log.e("ERR_GEN_SVC_FROM", e.getMessage());
            return "";
        }
    }

    public static String getServiceFromToday() {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(new Date());
            c.add(Calendar.MONTH, -1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            Log.e("ERR_GEN_SVC_FROM", e.getMessage());
            return "";
        }
    }

    public static String getServiceTo() {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(new Date());
            c.add(Calendar.DATE, -1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            Log.e("ERR_GEN_SVC_FROM", e.getMessage());
            return "";
        }
    }

    public static String getDueDate(String readDate) {
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(new Date());
            c.add(Calendar.DATE, +10);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            Log.e("ERR_GEN_SVC_FROM", e.getMessage());
            return "";
        }
    }

    public static String getPenalty(Bills bills) {
        try {
            if (bills != null) {
                return ObjectHelpers.roundTwo(Double.valueOf(bills.getNetAmount()) * .0226);
            } else {
                return "0";
            }
        } catch (Exception e) {
            Log.e("ERR_GETPENALTY", e.getMessage());
            return "0";
        }
    }

    public static String getPenaltyNoComma(Bills bills) {
        try {
            if (bills != null) {
                if (bills.getConsumerType().equals("RESIDENTIAL") | bills.getConsumerType().equals("RURAL RESIDENTIAL") | bills.getConsumerType().equals("RESIDENTIAL RURAL")) {
                    return "0";
                } else {
                    return (Double.valueOf(bills.getNetAmount()) * .05) + "";
                }
            } else {
                return "0";
            }
        } catch (Exception e) {
            Log.e("ERR_GETPENALTY", e.getMessage());
            return "0";
        }
    }

    public static String getLifelineRate(DownloadedPreviousReadings dpr, Bills bill, Rates rate) {
        try {
//            double kwhUsed = Double.valueOf(bill.getKwhUsed()) * Double.valueOf(bill.getMultiplier());
            double kwhUsed = Double.valueOf(bill.getKwhUsed());
            if (dpr.getChangeMeterAdditionalKwh() != null) {
                kwhUsed += Double.valueOf(Double.valueOf(dpr.getChangeMeterAdditionalKwh()));
            }

            double deductibles = Double.valueOf(bill.getGenerationSystemCharge()) +
                    Double.valueOf(bill.getACRM()) +
                    Double.valueOf(bill.getTransmissionDeliveryChargeKWH()) +
                    Double.valueOf(bill.getTransmissionDeliveryChargeKW()) +
                    Double.valueOf(bill.getSystemLossCharge()) +
                    Double.valueOf(bill.getOtherGenerationRateAdjustment()) +
                    Double.valueOf(bill.getOtherTransmissionCostAdjustmentKW()) +
                    Double.valueOf(bill.getOtherTransmissionCostAdjustmentKWH()) +
                    Double.valueOf(bill.getOtherSystemLossCostAdjustment()) +
                    Double.valueOf(bill.getDistributionDemandCharge()) +
                    Double.valueOf(bill.getDistributionSystemCharge()) +
                    Double.valueOf(bill.getSupplyRetailCustomerCharge()) +
                    Double.valueOf(bill.getSupplySystemCharge()) +
                    Double.valueOf(bill.getMeteringSystemCharge());

            if (dpr.getLifeliner() != null && dpr.getLifeliner().equals("Yes")) {
                if (kwhUsed >= 0 && kwhUsed <= 20) {
                    return "-" + ObjectHelpers.roundTwoNoComma(deductibles * 1);
                } else if (kwhUsed > 20 && kwhUsed <= 35) {
                    return "-" + ObjectHelpers.roundTwoNoComma(deductibles * .5);
                } else if (kwhUsed > 35 && kwhUsed <= 55) {
                    return "-" + ObjectHelpers.roundTwoNoComma(deductibles * .4);
                } else if (kwhUsed > 55 && kwhUsed <= 65) {
                    return "-" + ObjectHelpers.roundTwoNoComma(deductibles * .3);
                } else if (kwhUsed > 65 && kwhUsed <= 75) {
                    return "-" + ObjectHelpers.roundTwoNoComma(deductibles * .2);
                } else {
                    return ObjectHelpers.roundTwoNoComma(kwhUsed * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getLifelineRate())));
                }
            } else {
                return ObjectHelpers.roundTwoNoComma(kwhUsed * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getLifelineRate())));
            }

        } catch (Exception e) {
            Log.e("ERR_GET_LFLNE_RTE", e.getMessage());
            return "0";
        }
    }

    public static String getSeniorCitizenDiscount(DownloadedPreviousReadings dpr, Bills bill, Rates rate) {
        try {
            double kwhUsed = Double.valueOf(bill.getKwhUsed());
            if (dpr.getChangeMeterAdditionalKwh() != null) {
                kwhUsed += Double.valueOf(Double.valueOf(dpr.getChangeMeterAdditionalKwh()));
            }

            double deductibles = Double.valueOf(bill.getGenerationSystemCharge()) +
                    Double.valueOf(bill.getACRM()) +
                    Double.valueOf(bill.getTransmissionDeliveryChargeKWH()) +
                    Double.valueOf(bill.getTransmissionDeliveryChargeKW()) +
                    Double.valueOf(bill.getSystemLossCharge()) +
                    Double.valueOf(bill.getOtherGenerationRateAdjustment()) +
                    Double.valueOf(bill.getOtherTransmissionCostAdjustmentKW()) +
                    Double.valueOf(bill.getOtherTransmissionCostAdjustmentKWH()) +
                    Double.valueOf(bill.getOtherSystemLossCostAdjustment()) +
                    Double.valueOf(bill.getDistributionDemandCharge()) +
                    Double.valueOf(bill.getDistributionSystemCharge()) +
                    Double.valueOf(bill.getSupplyRetailCustomerCharge()) +
                    Double.valueOf(bill.getSupplySystemCharge()) +
                    Double.valueOf(bill.getMeteringSystemCharge()) +
                    Double.valueOf(bill.getLifelineRate());

            if (dpr.getSeniorCitizen() != null && dpr.getSeniorCitizen().equals("Yes") && kwhUsed <= 100) {
                return "-" + ObjectHelpers.roundTwoNoComma((deductibles * .05));
            } else {
                if (dpr.getAccountType().equals("RS") | dpr.getAccountType().equals("RP")) {
                    return ObjectHelpers.roundTwoNoComma(kwhUsed * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSeniorCitizenSubsidy())));
                } else {
                    return "0";
                }
            }
        } catch (Exception e) {
            Log.e("ERR_GET_SC_DSCNT", e.getMessage());
            return "0";
        }
    }

    public static String getNetAmount(DownloadedPreviousReadings dpr, Bills bill) {
        try {
            double amount = 0.0;

            String additionalCharges = bill.getAdditionalCharges() != null ? bill.getAdditionalCharges() : "0";

            amount = Double.valueOf(ObjectHelpers.doubleStringNull(bill.getGenerationSystemCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getTransmissionDeliveryChargeKW())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getTransmissionDeliveryChargeKWH())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getACRM())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getSystemLossCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getDistributionDemandCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getDistributionSystemCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getSupplyRetailCustomerCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getSupplySystemCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getMeteringRetailCustomerCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getMeteringSystemCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getRFSC())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getPowerActReduction())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getInterClassCrossSubsidyCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getPPARefund())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getMissionaryElectrificationCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getEnvironmentalCharge())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getStrandedContractCosts())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getNPCStrandedDebt())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getFeedInTariffAllowance())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getMissionaryElectrificationREDCI())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getMissionaryElectrificationSPUG())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getMissionaryElectrificationSPUGTRUEUP())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getGenerationVAT())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getTransmissionVAT())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getSystemLossVAT())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getDistributionVAT())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getOthersVAT())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getRealPropertyTax())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getACRMVAT())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getOtherGenerationRateAdjustment())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getOtherTransmissionCostAdjustmentKW())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getOtherTransmissionCostAdjustmentKWH())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getOtherSystemLossCostAdjustment())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getOtherLifelineRateCostAdjustment())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getSeniorCitizenDiscountAndSubsidyAdjustment())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getFranchiseTax())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getFranchiseTaxOthers())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getBusinessTax())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getSeniorCitizenSubsidy())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getLifelineRate())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getTermedPayments())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getAdvancedMaterialDeposit())) +
                Double.valueOf(ObjectHelpers.doubleStringNull(bill.getCustomerDeposit())) +
                Double.valueOf(additionalCharges);

            return ObjectHelpers.roundTwoNoComma(amount);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static String getDistributionVat(Bills bills) {
        try {
            return ObjectHelpers.roundTwoNoComma(getDistributionTotal(bills) * .12);
        } catch (Exception e) {
            Log.e("ERR_GET_DST_VAT", e.getMessage());
            return "0";
        }
    }

    public static Double getDistributionTotal(Bills bills) {
        try {
            double vatables = Double.valueOf(bills.getDistributionSystemCharge()) +
                    Double.valueOf(bills.getDistributionDemandCharge()) +
                    Double.valueOf(bills.getSupplyRetailCustomerCharge()) +
                    Double.valueOf(bills.getMeteringRetailCustomerCharge()) +
                    Double.valueOf(bills.getMeteringSystemCharge()) +
                    Double.valueOf(bills.getLifelineRate());

            return Double.valueOf(ObjectHelpers.roundTwoNoComma(vatables));
        } catch (Exception e) {
            Log.e("ERR_GET_DST_VAT", e.getMessage());
            return 0.0;
        }
    }

    public static String getFivePercent(Bills bills) {
        try {
            double vatables = Double.valueOf(bills.getDistributionSystemCharge()) +
                    Double.valueOf(bills.getDistributionDemandCharge()) +
                    Double.valueOf(bills.getSupplyRetailCustomerCharge()) +
                    Double.valueOf(bills.getMeteringRetailCustomerCharge()) +
                    Double.valueOf(bills.getLifelineRate()) +
                    Double.valueOf(bills.getInterClassCrossSubsidyCharge()) +
                    Double.valueOf(bills.getOtherLifelineRateCostAdjustment());

            return ObjectHelpers.roundTwoNoComma(vatables * .05);
        } catch (Exception e) {
            Log.e("ERR_GET_TWO_PERCENT", e.getMessage());
            return "0";
        }
    }

    public static String getTwoPercent(Bills bills) {
        try {
            double vatables = Double.valueOf(bills.getDistributionSystemCharge()) +
                    Double.valueOf(bills.getDistributionDemandCharge()) +
                    Double.valueOf(bills.getSupplyRetailCustomerCharge()) +
                    Double.valueOf(bills.getMeteringRetailCustomerCharge()) +
                    Double.valueOf(bills.getLifelineRate()) +
                    Double.valueOf(bills.getInterClassCrossSubsidyCharge()) +
                    Double.valueOf(bills.getOtherLifelineRateCostAdjustment());

            return ObjectHelpers.roundTwoNoComma(vatables * .02);
        } catch (Exception e) {
            Log.e("ERR_GET_TWO_PERCENT", e.getMessage());
            return "0";
        }
    }

    public static Double getBilledAmount(Bills bills) {
        try {
            double vatables = Double.valueOf(bills.getGenerationSystemCharge()) +
                    Double.valueOf(bills.getACRM()) +
                    Double.valueOf(bills.getTransmissionDeliveryChargeKWH()) +
                    Double.valueOf(bills.getSystemLossCharge()) +
                    Double.valueOf(bills.getDistributionDemandCharge()) +
                    Double.valueOf(bills.getDistributionSystemCharge()) +
                    Double.valueOf(bills.getSupplyRetailCustomerCharge()) +
                    Double.valueOf(bills.getSupplySystemCharge()) +
                    Double.valueOf(bills.getMeteringSystemCharge()) +
                    Double.valueOf(bills.getMeteringRetailCustomerCharge()) +
                    Double.valueOf(bills.getLifelineRate());

            return Double.valueOf(ObjectHelpers.roundTwoNoComma(vatables));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERR_GET_BL_AMNT", e.getMessage());
            return 0.0;
        }
    }

    public static Double getMaterialDeposit(DownloadedPreviousReadings dpr, Bills bills) {
        try {
            if (dpr.getAdvancedMaterialDepositStatus() != null && dpr.getAdvancedMaterialDepositStatus().equals("DEDUCTING")) {
                Double materialDeposit = ObjectHelpers.doubleStringNull(dpr.getAdvancedMaterialDeposit());
                if (materialDeposit > 0) {
                    if (dpr.getConnectionDate() != null && ObjectHelpers.formatShortDateInMillis(dpr.getConnectionDate()) > ObjectHelpers.formatShortDateInMillis("2023-01-01")) {
                        // 2023 AND ABOVE
                        double total = ObjectHelpers.doubleStringNull(bills.getDistributionSystemCharge()) +
                                ObjectHelpers.doubleStringNull(bills.getDistributionDemandCharge()) +
                                ObjectHelpers.doubleStringNull(bills.getSupplyRetailCustomerCharge()) +
                                ObjectHelpers.doubleStringNull(bills.getMeteringSystemCharge()) +
                                ObjectHelpers.doubleStringNull(bills.getMeteringRetailCustomerCharge());

                        double deposit = total * .25;

                        if (deposit >= materialDeposit) {
                            return -materialDeposit;
                        } else {
                            return -deposit;
                        }
                    } else {
                        // 2022 BELOW
                        double deposit = getBilledAmount(bills) * .75;

                        if (deposit >= materialDeposit) {
                            return -materialDeposit;
                        } else {
                            return -deposit;
                        }
                    }
                } else {
                    return 0.0;
                }
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERR_GET_MTRL_DPST", e.getMessage());
            return 0.0;
        }
    }

    public static Double getCustomerDeposit(DownloadedPreviousReadings dpr, Bills bill) {
        try {
            if (dpr.getCustomerDepositStatus() != null && dpr.getCustomerDepositStatus().equals("DEDUCTING")) {
                Double customerDeposit = ObjectHelpers.doubleStringNull(dpr.getCustomerDeposit());
                if (customerDeposit > 0) {
                    double billedAmount = getBilledAmount(bill);

                    if (customerDeposit > billedAmount) {
                        return -billedAmount;
                    } else {
                        return -customerDeposit;
                    }
                } else {
                    return 0.0;
                }
            } else {
                return 0.0;
            }
        } catch (Exception e) {
            Log.e("ERR_GET_CST_DPST", e.getMessage());
            e.printStackTrace();
            return  0.0;
        }
    }

    public static Bills generateRegularBill(Bills bill, DownloadedPreviousReadings dpr, Rates rate, Double kwhUsed, Double presReading, String userId, double termedPayments) {
        try {
            if (rate != null) {
                double effectiveRate = Double.valueOf(rate.getTotalRateVATIncluded());
                double multiplier = Double.valueOf(dpr.getMultiplier());
                String coreLossRaw = dpr.getCoreloss() != null ? dpr.getCoreloss() : "0";
                double coreloss = Double.valueOf(coreLossRaw);
                double kwhUsedFinal = multiplier * kwhUsed;
                if (dpr.getChangeMeterAdditionalKwh() != null) {
                    kwhUsedFinal += Double.valueOf(Double.valueOf(dpr.getChangeMeterAdditionalKwh()));
                }

                String arrearsLedger = dpr.getArrearsLedger() != null ? dpr.getArrearsLedger() : "0";
                double additionalCharges = Double.valueOf(arrearsLedger);

                if (bill != null) {
                    bill.setKwhUsed(kwhUsedFinal + "");
                    if (dpr.getChangeMeterAdditionalKwh() != null) {
                        bill.setAdditionalKwh(dpr.getChangeMeterAdditionalKwh());
                    }
                    bill.setPreviousKwh(dpr.getKwhUsed());
                    bill.setPresentKwh(presReading + "");
                    bill.setKwhAmount(ObjectHelpers.roundTwoNoComma(effectiveRate * kwhUsedFinal));
                    bill.setEffectiveRate(ObjectHelpers.roundTwoNoComma(effectiveRate));
                    bill.setAdditionalCharges(ObjectHelpers.roundTwoNoComma(additionalCharges));
                    bill.setDeductions("");

                    bill.setBillingDate(ObjectHelpers.getCurrentDate());
                    bill.setServiceDateFrom(dpr.getReadingTimestamp() != null ? dpr.getReadingTimestamp() : getServiceFromToday());
                    bill.setServiceDateTo(ObjectHelpers.getCurrentDate());
                    bill.setDueDate(getDueDate(ObjectHelpers.getCurrentDate()));
                    bill.setMeterNumber(dpr.getMeterSerial());
                    bill.setConsumerType(dpr.getAccountType());
                    bill.setBillType(dpr.getAccountType());

                    // COMPUTE RATES
                    bill.setGenerationSystemCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getGenerationSystemCharge()))));
                    bill.setTransmissionDeliveryChargeKW("0");
                    bill.setACRM(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getACRM()))));
                    bill.setTransmissionDeliveryChargeKWH(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionDeliveryChargeKWH()))));
                    bill.setSystemLossCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSystemLossCharge()))));
                    bill.setDistributionDemandCharge("0");
                    bill.setDistributionSystemCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getDistributionSystemCharge()))));
                    bill.setSupplyRetailCustomerCharge(kwhUsedFinal <= 0 ? "0" : ObjectHelpers.roundTwoNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSupplyRetailCustomerCharge()))));
                    bill.setSupplySystemCharge("0");
                    bill.setMeteringRetailCustomerCharge(ObjectHelpers.roundTwoNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMeteringRetailCustomerCharge()))));
                    bill.setMeteringSystemCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMeteringSystemCharge()))));
                    bill.setRFSC("0");
                    bill.setPowerActReduction(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getPowerActReduction()))));
                    bill.setInterClassCrossSubsidyCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getInterClassCrossSubsidyCharge()))));
                    bill.setPPARefund("0");
                    bill.setMissionaryElectrificationCharge("0");
                    bill.setEnvironmentalCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getEnvironmentalCharge()))));
                    bill.setStrandedContractCosts(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getStrandedContractCosts()))));
                    bill.setNPCStrandedDebt(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getNPCStrandedDebt()))));
                    bill.setFeedInTariffAllowance(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFeedInTariffAllowance()))));
                    bill.setMissionaryElectrificationREDCI(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationREDCI()))));
                    bill.setMissionaryElectrificationSPUG(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationSPUG()))));
                    bill.setMissionaryElectrificationSPUGTRUEUP(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationSPUGTRUEUP()))));
                    bill.setGenerationVAT(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getGenerationVAT()))));
                    bill.setTransmissionVAT(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionVAT()))));
                    bill.setSystemLossVAT(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSystemLossVAT()))));
                    bill.setACRMVAT(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getACRMVAT()))));
                    bill.setRealPropertyTax(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getRealPropertyTax()))));

                    bill.setOtherGenerationRateAdjustment("0");
                    bill.setOtherTransmissionCostAdjustmentKW("0");
                    bill.setOtherTransmissionCostAdjustmentKWH("0");
                    bill.setOtherSystemLossCostAdjustment("0");
                    bill.setOtherLifelineRateCostAdjustment("0");

                    bill.setLifelineRate(getLifelineRate(dpr, bill, rate));
                    bill.setDistributionVAT(getDistributionVat(bill));
                    bill.setFranchiseTax(ObjectHelpers.roundTwoNoComma(getDistributionTotal(bill) * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFranchiseTax()))));
                    bill.setFranchiseTaxOthers(ObjectHelpers.roundTwoNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(dpr.getPreviousSurcharges())) * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFranchiseTax()))));
                    bill.setBusinessTax(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getBusinessTax()))));

                    bill.setOthersVAT(ObjectHelpers.roundTwoNoComma((Double.valueOf(ObjectHelpers.doubleStringNull(bill.getFranchiseTax())) +
                            Double.valueOf(ObjectHelpers.doubleStringNull(bill.getFranchiseTaxOthers())) +
                            Double.valueOf(ObjectHelpers.doubleStringNull(dpr.getPreviousSurcharges()))) * .12));

                    bill.setTermedPayments(ObjectHelpers.roundTwoNoComma(termedPayments));

                    bill.setNetAmount(getNetAmount(dpr, bill));

                    bill.setAdvancedMaterialDeposit(ObjectHelpers.roundTwoNoComma(getMaterialDeposit(dpr, bill)));
                    bill.setNetAmount(ObjectHelpers.roundTwoNoComma(ObjectHelpers.doubleStringNull(bill.getNetAmount()) + ObjectHelpers.doubleStringNull(bill.getAdvancedMaterialDeposit())));
                    bill.setCustomerDeposit(ObjectHelpers.roundTwoNoComma(getCustomerDeposit(dpr, bill)));
                    bill.setNetAmount(ObjectHelpers.roundTwoNoComma(ObjectHelpers.doubleStringNull(bill.getNetAmount()) + ObjectHelpers.doubleStringNull(bill.getCustomerDeposit())));

                    bill.setSeniorCitizenSubsidy(getSeniorCitizenDiscount(dpr, bill, rate));

                    bill.setNetAmount(ObjectHelpers.roundTwoNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(bill.getNetAmount())) + Double.valueOf(ObjectHelpers.doubleStringNull(bill.getSeniorCitizenSubsidy()))));

                    bill.setBalance(bill.getNetAmount());
                    /**
                     * FOR DEPOSITS/PREPAYMENTS
                     */
                    if (dpr.getDeposit() != null) {
                        double depositAmount = Double.valueOf(dpr.getDeposit());
                        double netAmnt = Double.valueOf(bill.getNetAmount());
                        double difOfNetAmount = netAmnt - depositAmount;

                        if (difOfNetAmount > 0) {
                            bill.setNetAmount(ObjectHelpers.roundTwoNoComma(difOfNetAmount));
                            bill.setDeductedDeposit(ObjectHelpers.roundTwoNoComma(depositAmount));
                            bill.setExcessDeposit("0");
                        } else {
                            double depositVal = depositAmount - netAmnt;
                            bill.setNetAmount("0.0");
                            bill.setDeductedDeposit(ObjectHelpers.roundTwoNoComma(netAmnt));
                            bill.setExcessDeposit(ObjectHelpers.roundTwoNoComma(depositVal));
                        }
                    } else {
                        bill.setExcessDeposit("0");
                        bill.setDeductedDeposit("0");
                    }

                    /**
                     * FOR KATAS NG VAT
                     */
                    if (dpr.getKatasNgVat() != null) {
                        double katasAmount = Double.valueOf(dpr.getKatasNgVat());
                        double netAmnt = Double.valueOf(bill.getNetAmount());

                        if (netAmnt > 0) {
                            double difOfNetAmount = netAmnt - katasAmount;

                            if (difOfNetAmount > 0) {
                                bill.setNetAmount(ObjectHelpers.roundTwoNoComma(difOfNetAmount));
                                bill.setKatasNgVat(ObjectHelpers.roundTwoNoComma(katasAmount));
                            } else {
                                bill.setNetAmount("0.0");
                                bill.setKatasNgVat(ObjectHelpers.roundTwoNoComma(netAmnt));
                            }
                        } else {
                            bill.setKatasNgVat("0");
                        }
                    } else {
                        bill.setKatasNgVat("0");
                    }

                    bill.setUserId(userId);
                    bill.setBilledFrom("APP");
                    bill.setUploadStatus("UPLOADABLE");
                } else {
                    bill  = new Bills();

                    bill.setId(ObjectHelpers.generateIDandRandString());
                    bill.setBillNumber(generateBillNumber(dpr.getAreaCode()));
                    bill.setAccountNumber(dpr.getAccountId());
                    bill.setServicePeriod(dpr.getServicePeriod());
                    bill.setMultiplier(dpr.getMultiplier());
                    bill.setCoreloss(coreLossRaw);
                    bill.setKwhUsed(kwhUsedFinal + "");
                    if (dpr.getChangeMeterAdditionalKwh() != null) {
                        bill.setAdditionalKwh(dpr.getChangeMeterAdditionalKwh());
                    }
                    bill.setPreviousKwh(dpr.getKwhUsed());
                    bill.setPresentKwh(presReading + "");
                    bill.setKwhAmount(ObjectHelpers.roundTwoNoComma(effectiveRate * kwhUsedFinal));
                    bill.setEffectiveRate(ObjectHelpers.roundTwoNoComma(effectiveRate));
                    bill.setAdditionalCharges(ObjectHelpers.roundTwoNoComma(additionalCharges));
                    bill.setDeductions("");

                    bill.setBillingDate(ObjectHelpers.getCurrentDate());
                    bill.setServiceDateFrom(dpr.getReadingTimestamp() != null ? dpr.getReadingTimestamp() : getServiceFromToday());
                    bill.setServiceDateTo(ObjectHelpers.getCurrentDate());
                    bill.setDueDate(getDueDate(ObjectHelpers.getCurrentDate()));
                    bill.setMeterNumber(dpr.getMeterSerial());
                    bill.setConsumerType(dpr.getAccountType());
                    bill.setBillType(dpr.getAccountType());

                    // COMPUTE RATES
                    bill.setGenerationSystemCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getGenerationSystemCharge()))));
                    bill.setTransmissionDeliveryChargeKW("0");
                    bill.setACRM(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getACRM()))));
                    bill.setTransmissionDeliveryChargeKWH(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionDeliveryChargeKWH()))));
                    bill.setSystemLossCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSystemLossCharge()))));
                    bill.setDistributionDemandCharge("0");
                    bill.setDistributionSystemCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getDistributionSystemCharge()))));
                    bill.setSupplyRetailCustomerCharge(kwhUsedFinal <= 0 ? "0" : ObjectHelpers.roundTwoNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSupplyRetailCustomerCharge()))));
                    bill.setSupplySystemCharge("0");
                    bill.setMeteringRetailCustomerCharge(ObjectHelpers.roundTwoNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMeteringRetailCustomerCharge()))));
                    bill.setMeteringSystemCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMeteringSystemCharge()))));
                    bill.setRFSC("0");
                    bill.setPowerActReduction(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getPowerActReduction()))));
                    bill.setInterClassCrossSubsidyCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getInterClassCrossSubsidyCharge()))));
                    bill.setPPARefund("0");
                    bill.setMissionaryElectrificationCharge("0");
                    bill.setEnvironmentalCharge(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getEnvironmentalCharge()))));
                    bill.setStrandedContractCosts(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getStrandedContractCosts()))));
                    bill.setNPCStrandedDebt(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getNPCStrandedDebt()))));
                    bill.setFeedInTariffAllowance(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFeedInTariffAllowance()))));
                    bill.setMissionaryElectrificationREDCI(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationREDCI()))));
                    bill.setMissionaryElectrificationSPUG(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationSPUG()))));
                    bill.setMissionaryElectrificationSPUGTRUEUP(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getMissionaryElectrificationSPUGTRUEUP()))));
                    bill.setGenerationVAT(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getGenerationVAT()))));
                    bill.setTransmissionVAT(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getTransmissionVAT()))));
                    bill.setSystemLossVAT(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getSystemLossVAT()))));
                    bill.setACRMVAT(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getACRMVAT()))));
                    bill.setRealPropertyTax(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getRealPropertyTax()))));

                    bill.setOtherGenerationRateAdjustment("0");
                    bill.setOtherTransmissionCostAdjustmentKW("0");
                    bill.setOtherTransmissionCostAdjustmentKWH("0");
                    bill.setOtherSystemLossCostAdjustment("0");
                    bill.setOtherLifelineRateCostAdjustment("0");

                    bill.setLifelineRate(getLifelineRate(dpr, bill, rate));
                    bill.setDistributionVAT(getDistributionVat(bill));
                    bill.setFranchiseTax(ObjectHelpers.roundTwoNoComma(getDistributionTotal(bill) * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFranchiseTax()))));
                    bill.setFranchiseTaxOthers(ObjectHelpers.roundTwoNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(dpr.getPreviousSurcharges())) * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getFranchiseTax()))));
                    bill.setBusinessTax(ObjectHelpers.roundTwoNoComma(kwhUsedFinal * Double.valueOf(ObjectHelpers.doubleStringNull(rate.getBusinessTax()))));

                    bill.setOthersVAT(ObjectHelpers.roundTwoNoComma((Double.valueOf(ObjectHelpers.doubleStringNull(bill.getFranchiseTax())) +
                            Double.valueOf(ObjectHelpers.doubleStringNull(bill.getFranchiseTaxOthers())) +
                            Double.valueOf(ObjectHelpers.doubleStringNull(dpr.getPreviousSurcharges()))) * .12));

                    bill.setTermedPayments(ObjectHelpers.roundTwoNoComma(termedPayments));

                    bill.setNetAmount(getNetAmount(dpr, bill));

                    bill.setAdvancedMaterialDeposit(ObjectHelpers.roundTwoNoComma(getMaterialDeposit(dpr, bill)));
                    bill.setNetAmount(ObjectHelpers.roundTwoNoComma(ObjectHelpers.doubleStringNull(bill.getNetAmount()) + ObjectHelpers.doubleStringNull(bill.getAdvancedMaterialDeposit())));
                    bill.setCustomerDeposit(ObjectHelpers.roundTwoNoComma(getCustomerDeposit(dpr, bill)));
                    bill.setNetAmount(ObjectHelpers.roundTwoNoComma(ObjectHelpers.doubleStringNull(bill.getNetAmount()) + ObjectHelpers.doubleStringNull(bill.getCustomerDeposit())));

                    bill.setSeniorCitizenSubsidy(getSeniorCitizenDiscount(dpr, bill, rate));

                    bill.setNetAmount(ObjectHelpers.roundTwoNoComma(Double.valueOf(ObjectHelpers.doubleStringNull(bill.getNetAmount())) + Double.valueOf(ObjectHelpers.doubleStringNull(bill.getSeniorCitizenSubsidy()))));

                    bill.setBalance(bill.getNetAmount());
                    /**
                     * FOR DEPOSITS/PREPAYMENTS
                     */
                    if (dpr.getDeposit() != null) {
                        double depositAmount = Double.valueOf(dpr.getDeposit());
                        double netAmnt = Double.valueOf(bill.getNetAmount());
                        double difOfNetAmount = netAmnt - depositAmount;

                        if (difOfNetAmount > 0) {
                            bill.setNetAmount(ObjectHelpers.roundTwoNoComma(difOfNetAmount));
                            bill.setDeductedDeposit(ObjectHelpers.roundTwoNoComma(depositAmount));
                            bill.setExcessDeposit("0");
                        } else {
                            double depositVal = depositAmount - netAmnt;
                            bill.setNetAmount("0.0");
                            bill.setDeductedDeposit(ObjectHelpers.roundTwoNoComma(netAmnt));
                            bill.setExcessDeposit(ObjectHelpers.roundTwoNoComma(depositVal));
                        }
                    } else {
                        bill.setExcessDeposit("0");
                        bill.setDeductedDeposit("0");
                    }

                    /**
                     * FOR KATAS NG VAT
                     */
                    if (dpr.getKatasNgVat() != null) {
                        double katasAmount = Double.valueOf(dpr.getKatasNgVat());
                        double netAmnt = Double.valueOf(bill.getNetAmount());

                        if (netAmnt > 0) {
                            double difOfNetAmount = netAmnt - katasAmount;

                            if (difOfNetAmount > 0) {
                                bill.setNetAmount(ObjectHelpers.roundTwoNoComma(difOfNetAmount));
                                bill.setKatasNgVat(ObjectHelpers.roundTwoNoComma(katasAmount));
                            } else {
                                bill.setNetAmount("0.0");
                                bill.setKatasNgVat(ObjectHelpers.roundTwoNoComma(netAmnt));
                            }
                        } else {
                            bill.setKatasNgVat("0");
                        }
                    } else {
                        bill.setKatasNgVat("0");
                    }

                    bill.setUserId(userId);
                    bill.setBilledFrom("APP");
                    bill.setUploadStatus("UPLOADABLE");
                }

                return bill;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("ERR_GENRTE_BILL", e.getMessage());
            return null;
        }
    }

    public static String getAccountType(DownloadedPreviousReadings dpr) {
        if (dpr.getAccountType() != null) {
            if (dpr.getAccountType().equals("RURAL RESIDENTIAL")) {
                return "RESIDENTIAL";
            } else {
                return dpr.getAccountType();
            }
        } else {
            return "RESIDENTIAL";
        }

    }

    public static double getNearestRoundCeiling(double x) {
//        final double pow = Math.pow(10, -Math.floor(Math.log10(x)));
//        return Math.ceil(x * pow) / pow;
        return getResetValue(x);
    }

    public static double getResetValue(double x) {
        String no = String.valueOf(x).substring(0,1);
        int num = (int)x;
        int firstD = Integer.valueOf(no);
        String val = (firstD + 1) + getNumZeros(String.valueOf(num).length());
        return Double.valueOf(val);
    }

    public static String getNumZeros(int count) {
        String zeros = "";
        for (int i=0; i<count-1; i++) {
            zeros += "0";
        }
        return zeros;
    }
}
