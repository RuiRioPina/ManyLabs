package app.domain.model;

import app.domain.model.matcp.LinearRegression;
import app.domain.model.matcp.MultiLinearRegression;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NHSReport {
    private LinearRegression linearRegression = null;
    private MultiLinearRegression multiLinearRegression = null;
    private double[] receivedYData;
    private double[] receivedX1Data;
    private double[] receivedX2Data;

    /**
     * Constructor for the NHS Report.
     * @param linearRegression simple linear Regression to be used for the report.
     * @param receivedYData received y data for the predictions
     * @param receivedXData received x data for the predictions
     */
    public NHSReport(LinearRegression linearRegression, double[] receivedYData, double[] receivedXData) {
        this.receivedYData = receivedYData;
        this.linearRegression = linearRegression;
        this.receivedX1Data = receivedXData;
    }
    /**
     * Constructor for the NHS Report.
     * @param multiLinearRegression multi linear Regression to be used for the report.
     * @param receivedYData received y data for the predictions
     * @param receivedX1Data received x1 data for the predictions
     * @param receivedX2Data received x2 data for the predictions
     */
    public NHSReport(MultiLinearRegression multiLinearRegression, double[] receivedX1Data, double[] receivedX2Data, double[] receivedYData) {
        this.multiLinearRegression = multiLinearRegression;
        this.receivedYData = receivedYData;
        this.receivedX1Data = receivedX1Data;
        this.receivedX2Data = receivedX2Data;

    }

    /**
     * Creates a string for the first lines in the final String
     * @return String for the first line
     */
    private String regressionModelLine() {
        //lineEquation = getLine();
        if (this.multiLinearRegression == null && this.linearRegression != null) {
            return String.format("The regression model fitted using data from the interval %n %s%n", linearRegression);
        } else if (this.multiLinearRegression != null && this.linearRegression == null) {
            return String.format("The regression model fitted using data from the interval %n %s%n", multiLinearRegression);
        }
        return String.format("You have must introduce either Multilinear / Simple Linear Regression");
    }

    /**
     * Creates a string for the second lines in the final String
     * @return String for the second line
     */
    private String otherStatistics() {
        if (this.multiLinearRegression == null && this.linearRegression != null) {
            return String.format("%nOther statistics %n R2 = %s %n R2adjusted = %s %n R = %s %n ", this.linearRegression.R2(), this.linearRegression.R2Adjusted(), this.linearRegression.R());
        } else if (this.multiLinearRegression != null && this.linearRegression == null) {
            return String.format("%nOther statistics %n R2 = %s %n R2adjusted = %s %n R = %s %n ", this.multiLinearRegression.r2(), this.multiLinearRegression.r2Adjusted(), this.multiLinearRegression.r());
        }
        return "123123123";
    }

    /**
     * * Creates a string for the thi lines in the final String
     * @param aParemeterSignificance significance for the a parameter hypothesis test /B0 hypothesis test significance
     * @param aTestParameter a parameter used for the test /B1 hypothesis test significance
     * @param bParameterSignificance significance for the b parameter hypothesis test /B2 hypothesis test significance
     * @param bTestParameter b parameter used for the test
     * @return String for the third line
     */
    private String hypothesisTests(double aParemeterSignificance, double aTestParameter, double bParameterSignificance, double bTestParameter) {
        if (this.multiLinearRegression == null && this.linearRegression != null) {
            return String.format("%nHypothesis tests for regression coefficients \n" +
                    "H0: a=%s (b=%s) H1: a<>%s (b<>%s) %n " +
                    "tobs(a)=%.4f tobs(b)=%.4f %n" +
                    "Decision:%n" +
                    "Regarding the test for the a parameter h0 is %s %n" +
                    "Regarding the test for the b parameter h0 is %s %n", aTestParameter, bTestParameter, aTestParameter, bTestParameter, this.linearRegression.testCalculationforAparameter(aTestParameter), this.linearRegression.testCalculationforBparameter(bTestParameter), isARejectedOrNot(aTestParameter, aParemeterSignificance), isBRejectedOrNot(aTestParameter, bParameterSignificance));

        } else if (this.multiLinearRegression != null && this.linearRegression == null) {
            return String.format("%nHypothesis tests for regression coefficients \n" +
                            "H0: b0=0 (x1=0) (x2=0) H1: b0<>0 (x1<>0) (x2<>0) %n " +
                            "tobs(b0)=%.4f tobs(b1)=%.4f tobs(b2)=%.4f  %n" +
                            "Decision:%n" +
                            "Regarding the test for the b0 parameter h0 is %s %n" +
                            "Regarding the test for the b1 parameter h0 is %s %n" +
                            "Regarding the test for the b2 parameter h0 is %s %n", this.multiLinearRegression.hypothesisB0()
                    , this.multiLinearRegression.hypothesisB1(), this.multiLinearRegression.hypothesisB2(), isMultiLinearRegressionRejected(multiLinearRegression.getTStudentFromTable(aParemeterSignificance),multiLinearRegression.hypothesisB0()), isMultiLinearRegressionRejected(bParameterSignificance,multiLinearRegression.hypothesisB1()), isMultiLinearRegressionRejected(aTestParameter, multiLinearRegression.hypothesisB2()));

        }
        return "1231321312";
    }

    /**
     * Creates a string for the fourth lines in the final String
     * @return String for the fourth lines
     */
    private String Anova() {
        if (this.multiLinearRegression == null && this.linearRegression != null) {
            return String.format("%nSignificance model with Anova \n" +
                    "H0: b=0  H1:b<>0 \n %15s %12s %12s %12s \n" +
                    "Regression %8.4f %12.4f %12.4f\t%12.4f \nResidual %10.4f  %11.4f %12.4f \n Total %12.4f %12.4f", "df", "SS", "MS", "F", (double) linearRegression.getRegressionDF(), linearRegression.calculateAnovaSR(), linearRegression.calculateAnovaMSR(), linearRegression.calculateTestF(), (double) linearRegression.getResidualDF(), linearRegression.calculateAnovaSE(), linearRegression.calculateAnovaMSE(), (double)linearRegression.getRegressionDF()+linearRegression.getResidualDF(), linearRegression.calculateAnovaST());
        } else if (this.multiLinearRegression != null && this.linearRegression == null) {
            return String.format("%nSignificance model with Anova \n" +
                    "H0: b=0  H1:b<>0 \n %15s %12s %12s %12s \n" +
                    "Regression %8.4f %12.4f %12.4f\t%12.4f \nResidual %10.4f  %11.4f %12.4f \n Total %12.4f %12.4f", "df", "SS", "MS", "F", multiLinearRegression.regressionDegreesOfFreedom(), multiLinearRegression.sqr(), multiLinearRegression.mqr(), multiLinearRegression.testStatisticF(), multiLinearRegression.errorDegreesOfFreedom(), multiLinearRegression.sqe(), multiLinearRegression.mqe(), multiLinearRegression.totalDegreesOfFreedom(), multiLinearRegression.sqt());
        }
        return "There was an error in the making of the ANOVA please try again.";
    }

    /**
     * Creates a string for the fifth lines in the final String
     * @param fTestDecisionSignificance significance to be used
     * @return String for the fifth lines
     */
    private String decisionF(double fTestDecisionSignificance) {
        if (this.multiLinearRegression == null && this.linearRegression != null) {
            return String.format("\n Decision: f %n" +
                    "0 > f%.2f,(%d.%d)=%.4f%n" +
                    "%s", fTestDecisionSignificance, linearRegression.getRegressionDF(), linearRegression.getResidualDF(), linearRegression.getFSnedcorFromTable(fTestDecisionSignificance), isFRejectedOrNot(fTestDecisionSignificance));
        }
        assert multiLinearRegression != null;
        return String.format("\n Decision: f %n" +
                "0 > f%.2f,(%d.%d)=%.4f%n" +
                "%s", fTestDecisionSignificance, (int) multiLinearRegression.regressionDegreesOfFreedom(), (int) multiLinearRegression.errorDegreesOfFreedom(), multiLinearRegression.getFSnedcorFromTable(fTestDecisionSignificance), isFRejectedOrNot(fTestDecisionSignificance));
    }

    private static String predictionValues(String date, int positiveCases, double estimatedCases, String intervals) {
        return String.format("%n%4s %15d  %43.4f %45s %n", date, positiveCases, estimatedCases, intervals);
    }

    /**
     * Creates a string for the sixth lines in the final String
     * @param confidenceIntervalSignificance significance to be used
     * @return string for the sixth lines.
     */
    private static String linePredictionValues(double confidenceIntervalSignificance) {
        //confidencenceLevel = getConfidenceLevel();
        return String.format("%n%n%s %40s %40s %30s", "Date", "Number of OBSERVED positive cases", "Number of ESTIMATED positive cases ", confidenceIntervalSignificance*100 + "% intervals \n");
    }

    /**
     * Checks if the A hypothesis test is rejected or not
     * @param TestParameter chosen parameter
     * @param TestSignificance significance to be used
     * @return a string that tells if the test was rejected or not
     */
    private String isARejectedOrNot(double TestParameter, double TestSignificance) {
        if (linearRegression != null && multiLinearRegression == null) {
            if (this.linearRegression.testCalculationforAparameter(TestParameter) > this.linearRegression.getTStudentFromTable(TestSignificance)) {
                return "rejected";
            } else return "not rejected";
        } else if (TestParameter > this.multiLinearRegression.getTStudentFromTable(TestSignificance)) {
            return "rejected";
        } else {
            return "not rejected";
        }
    }
    /**
     * Checks if the B hypothesis test is rejected or not
     * @param TestParameter chosen parameter
     * @param TestSignificance significance to be used
     * @return a string that tells if the test was rejected or not
     */
    private String isBRejectedOrNot(double TestParameter, double TestSignificance) {
        if (linearRegression != null && multiLinearRegression == null) {
            if (this.linearRegression.testCalculationforBparameter(TestParameter) > this.linearRegression.getTStudentFromTable(TestSignificance)) {
                return "rejected";
            } else return "not rejected";
        } else if (TestParameter > this.multiLinearRegression.getTStudentFromTable(TestSignificance)) {
            return "rejected";
        } else {
            return "not rejected";
        }
    }

    /**
     * Checks if the F hypothesis test is rejected or not
     * @param FTestSignificance significance to be used
     * @return string detailing whether the regression model is significant or not
     */
    private String isFRejectedOrNot(double FTestSignificance) {
        if (linearRegression != null && multiLinearRegression == null) {
            if (linearRegression.calculateTestF() > linearRegression.getFSnedcorFromTable(FTestSignificance)) {
                return String.format("Reject H0 %n The regression model is significant %n");
            } else return String.format("Not Reject H0 %n The regression model is not significant %n\"");
        } else if (linearRegression == null && multiLinearRegression != null) {
            if (multiLinearRegression.testStatisticF() > multiLinearRegression.getFSnedcorFromTable(FTestSignificance)) {
                return String.format("Reject H0 %n The regression model is significant %n");
            } else return String.format("Not Reject H0 %n The regression model is not significant %n\"");
        }
        return "1t31t";
    }

    /**
     * Checks if the multi linear regression hypothesis is rejected and gives a string
     * @param significance significance to be used
     * @param hypothesis hypothesis test statistic to be used
     * @return a string that tells if the test was rejected or not
     */
    private  String isMultiLinearRegressionRejected(double significance,double hypothesis){
        if (Math.abs(hypothesis)> multiLinearRegression.getTStudentFromTable(significance)){
            return "rejected";
        }else return "not rejected";
    }

    /**
     * gives a string for the final table.
     * @param olderDate older date to be printed
     * @param newerDate newer date to be printed
     * @param confidenceIntervalSignificance significance of the confidence interval
     * @param identifier identifier to determine whether the string should be made using weeks or days
     * @return string for the final table in the report
     */
    public String printFinalTable(Calendar olderDate, Calendar newerDate, double confidenceIntervalSignificance, String identifier) {
        if (identifier.equalsIgnoreCase("days")) {
            int i = 1;
            StringBuilder resultString = new StringBuilder();
            Calendar olderDateUsed = (Calendar) olderDate.clone();
            Calendar newerDateUsed = (Calendar) newerDate.clone();
            if (linearRegression != null && multiLinearRegression == null) {
                do {
                    if (olderDateUsed.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        if (i != 1) {
                            showDate(confidenceIntervalSignificance, i, resultString, olderDateUsed);
                        }
                        i++;
                    }
                    olderDateUsed.add(Calendar.DAY_OF_MONTH, 1);

                } while (newerDateUsed.after(olderDateUsed));
            } else if (linearRegression == null && multiLinearRegression != null) {
                do {
                    if (olderDateUsed.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                        if (i != 1) {
                            showDateWeeks(confidenceIntervalSignificance, i, resultString, olderDateUsed);
                        }
                        i++;
                    }
                    olderDateUsed.add(Calendar.DAY_OF_MONTH, 1);

                } while (newerDateUsed.after(olderDateUsed));
            }
            return resultString.toString();
        }
        if (identifier.equalsIgnoreCase("weeks")) {
            int i = 1;
            StringBuilder resultString = new StringBuilder();
            Calendar olderDateUsed = (Calendar) olderDate.clone();
            Calendar newerDateUsed = (Calendar) newerDate.clone();
            if (linearRegression != null && multiLinearRegression == null) {
                do {
                    if (olderDateUsed.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {

                        showDate(confidenceIntervalSignificance, i, resultString, olderDateUsed);

                        i++;
                    }
                    olderDateUsed.add(Calendar.DAY_OF_MONTH, 1);

                } while (newerDateUsed.after(olderDateUsed));
            } else if (linearRegression == null && multiLinearRegression != null) {
                do {
                if (olderDateUsed.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {

                    showDateWeeks(confidenceIntervalSignificance, i, resultString, olderDateUsed);

                    i++;
                }
                olderDateUsed.add(Calendar.DAY_OF_MONTH, 1);
            } while (newerDateUsed.after(olderDateUsed));
            }
            return resultString.toString();
        } else return null;
    }


    private void showDateWeeks(double confidenceIntervalSignificance, int i, StringBuilder resultString, Calendar olderDateUsed) {
        if (this.linearRegression==null&& this.multiLinearRegression!=null) {
            resultString.append(String.format("%d/%d/%d %30.2f %30.2f %30.2f-%.2f %n", olderDateUsed.get(Calendar.DAY_OF_MONTH), olderDateUsed.get(Calendar.MONTH) + 1, olderDateUsed.get(Calendar.YEAR), receivedYData[i - 1], multiLinearRegression.predict(receivedX1Data[i - 1],receivedX2Data[i-1]), multiLinearRegression.predict(receivedX1Data[i - 1],receivedX2Data[i-1]) - multiLinearRegression.expectedValueTestConfidenceIntervalAuxiliaryCalculus(confidenceIntervalSignificance, receivedX1Data[i - 1], receivedX2Data[i - 1]), multiLinearRegression.predict(receivedX1Data[i - 1],receivedX2Data[i-1]) + multiLinearRegression.expectedValueTestConfidenceIntervalAuxiliaryCalculus(confidenceIntervalSignificance, receivedX1Data[i - 1], receivedX2Data[i - 1])));
        }else  resultString.append(String.format("%d/%d/%d %30.2f %30.2f %30.2f-%.2f %n", olderDateUsed.get(Calendar.DAY_OF_MONTH), olderDateUsed.get(Calendar.MONTH) + 1, olderDateUsed.get(Calendar.YEAR), receivedYData[i - 1], linearRegression.predict(receivedX1Data[i - 1]), linearRegression.predict(receivedX1Data[i - 1]) - linearRegression.delta(confidenceIntervalSignificance,linearRegression.predict(receivedX1Data[i-1])),linearRegression.predict(receivedX1Data[i-1])+linearRegression.delta(confidenceIntervalSignificance,linearRegression.predict(receivedX1Data[i-1]))));
    }

    private void showDate(double confidenceIntervalSignificance, int i, StringBuilder resultString, Calendar olderDateUsed) {
        if (this.linearRegression==null&&this.multiLinearRegression!=null) {
            resultString.append(String.format("%d/%d/%d %30.2f %30.2f %30.2f-%.2f %n", olderDateUsed.get(Calendar.DAY_OF_MONTH), olderDateUsed.get(Calendar.MONTH) + 1, olderDateUsed.get(Calendar.YEAR), receivedYData[i - 1],multiLinearRegression.predict(receivedX1Data[i - 1],receivedX2Data[i-1]), multiLinearRegression.predict(receivedX1Data[i-1],receivedX2Data[i-1]) - multiLinearRegression.expectedValueTestConfidenceIntervalAuxiliaryCalculus(confidenceIntervalSignificance,receivedX1Data[i-1],receivedX2Data[i-1]), multiLinearRegression.predict(receivedX1Data[i-1],receivedX2Data[i-1]) + multiLinearRegression.expectedValueTestConfidenceIntervalAuxiliaryCalculus(confidenceIntervalSignificance, receivedX1Data[i - 1],receivedX2Data[i-1])));
        }else resultString.append(String.format("%d/%d/%d %30.2f %30.2f %30.2f-%.2f %n", olderDateUsed.get(Calendar.DAY_OF_MONTH), olderDateUsed.get(Calendar.MONTH) + 1, olderDateUsed.get(Calendar.YEAR), receivedYData[i - 1],linearRegression.predict(receivedX1Data[i - 1]), linearRegression.predict(receivedX1Data[i-1]) - linearRegression.delta(confidenceIntervalSignificance,receivedX1Data[i-1]),linearRegression.predict(receivedX1Data[i-1])+linearRegression.delta(confidenceIntervalSignificance,receivedX1Data[i-1])));
    }

    /**
     * returns the final string
     * @param olderDate older date to be printed
     * @param newerDate newer date to be printed
     * @param aParameterSignificance a parameter hypothe
     * @param aTestParameter a parameter used for the test /B1 hypothesis test significance
     * @param bTestSignificance b tests significance
     * @param bTestParameter b parameter used for the test
     * @param fTestSignificance f test significance
     * @param confidenceIntervalSignificance confidence interval significance value
     * @param identifier identifier to determine whether the string should be made using weeks or days
     * @return the final string
     */

    public String getReportString(Calendar olderDate, Calendar newerDate, double aParameterSignificance, double aTestParameter, double bTestSignificance, double bTestParameter, double fTestSignificance, double confidenceIntervalSignificance, String identifier) {

        if (linearRegression != null && multiLinearRegression == null) {
            return regressionModelLine() + otherStatistics() + hypothesisTests(aParameterSignificance, aTestParameter, bTestSignificance, bTestParameter) + Anova() + decisionF(fTestSignificance) + linePredictionValues(confidenceIntervalSignificance) + printFinalTable(olderDate, newerDate, confidenceIntervalSignificance, identifier);
        }
        return regressionModelLine() + otherStatistics() + hypothesisTests(aParameterSignificance, aTestParameter, bTestSignificance, bTestParameter) + Anova() + decisionF(fTestSignificance) + linePredictionValues(confidenceIntervalSignificance) + printFinalTable(olderDate, newerDate, confidenceIntervalSignificance, identifier);
    }
    }



