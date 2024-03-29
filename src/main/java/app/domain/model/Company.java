package app.domain.model;

import app.controller.App;
import app.domain.model.matcp.LinearRegression;
import app.domain.model.matcp.MultiLinearRegression;
import app.domain.shared.Constants;
import app.domain.store.*;
import app.ui.console.utils.Utils;
import auth.AuthFacade;
import com.nhs.report.Report2NHS;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Paulo Maio <pam@isep.ipp.pt>
 */
public class Company implements Serializable {

    private int numberOfEmployees;
    private long lastBarcode;
    private int testCode;
    private final String designation;
    private final AuthFacade authFacade;
    private final ParameterCategoryStore parameterCategoryStore;
    private final TestTypeStore testTypeStore;
    private final ParameterStore parameterStore;
    private EmployeeStore employeeStore;
    private final ResultOfTestStore resultOfTestStore;
    private RoleStore roleStore;
    private final ClientList clientList;
    private final ClinicalAnalysisLaboratoryStore clinicalAnalysisLaboratoryStore;
    private final ReportStore reportStore;
    private ClinicalAnalysisLaboratory cla;
    private TestStore testStore;
    private LabCoordinatorStore labCoorStore;
    // Used to pass the imported tests beetween classes.
    private List<Test> importedTests = new ArrayList<>();

    public Company(String designation) {
        if (StringUtils.isBlank(designation))
            throw new IllegalArgumentException("Designation cannot be blank.");

        this.designation = designation;
        this.authFacade = new AuthFacade();
        this.parameterCategoryStore = new ParameterCategoryStore();
        this.testTypeStore = new TestTypeStore();
        this.parameterStore = new ParameterStore();
        this.clientList = new ClientList();
        this.employeeStore = new EmployeeStore();
        this.resultOfTestStore = new ResultOfTestStore();
        this.roleStore = new RoleStore();
        this.employeeStore = new EmployeeStore();
        this.roleStore = new RoleStore();
        this.clinicalAnalysisLaboratoryStore = new ClinicalAnalysisLaboratoryStore();
        this.numberOfEmployees = 0;
        this.lastBarcode = 0;
        this.testCode = 0;
        this.reportStore = new ReportStore();
        this.cla = null;
        this.testStore = new TestStore();
        this.labCoorStore = new LabCoordinatorStore();
    }

    public List<Test> getImportedTests() {
        return this.importedTests;
    }

    public void setImportedTests(List<Test> importedTests) {
        this.importedTests = importedTests;
    }

    public ClinicalAnalysisLaboratory getCLA() {
        return this.cla;
    }

    public int getTestCode() {
        return testCode;
    }

    /**
     * Getter for the number of Employees in the company.
     *
     * @return number of employees.
     */
    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public String getDesignation() {
        return designation;
    }

    public AuthFacade getAuthFacade() {
        return authFacade;
    }

    public ParameterCategoryStore getParameterCategoryStore() {
        return this.parameterCategoryStore;
    }

    public TestTypeStore getTestTypeStore() {
        return this.testTypeStore;
    }

    public ParameterStore getParameterStore() {
        return this.parameterStore;
    }

    /**
     * Getter for the Employee Store that the company is using.
     *
     * @return Employee Store that contains the Employees in the company.
     */
    public EmployeeStore getEmployeeStore() {
        return this.employeeStore;
    }

    public ClientList getClientList() {
        return this.clientList;
    }

    public TestStore getTestStore() {
        return this.testStore;
    }

    public LabCoordinatorStore getLabCoorStore() {
        return this.labCoorStore;
    }

    public ReportStore getReportStore() {
        return this.reportStore;
    }

    /**
     * Getter for the Role Store that the company is using.
     *
     * @return Role Store that contains the Roles in the company.
     */
    public RoleStore getRoleStore() {
        return this.roleStore;
    }


    public String getNextBarcode() {
        this.lastBarcode++;
        return String.format("%011d", this.lastBarcode);
    }

    public List<Test> getAllTest() {
        List<Test> testes = new ArrayList<>();
        testes.addAll(getTestStore().getTests());
        return testes;
    }

    public TestStore getAllTestCompleted() {
        List<Test> tests = new ArrayList<>();
        tests.addAll(testStore.getTestsWithoutDiagnosis());
        testStore = new TestStore(tests);
        return testStore;
    }

    public Calendar tStringToCalendar(String txt) {
        String message = "Wrong date";
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date dt = df.parse(txt);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dt);
            return calendar;
        } catch (Exception ex) {
            Utils.log(message);
        }
        return null;
    }

    public ClinicalAnalysisLaboratoryStore getClinicalAnalysisLaboratoryStore() {
        return this.clinicalAnalysisLaboratoryStore;
    }


    public void setTestCode(int number) {
        this.testCode = number;
    }

    public void setCLA(ClinicalAnalysisLaboratory cla) {
        this.cla = cla;
    }


    /**
     * Setter for the number of Employees in the company
     *
     * @param numberOfEmployees- new number of Employees.
     */
    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;

    }

    /**
     * Simulates sending an email to the Client by writing the password and some other information to a txt file.
     *
     * @param c instance of the Client class
     * @return true if it was able to create and write to the file
     * @throws IOException          if the file to be written to doesn't exist
     * @throws InterruptedException if the thread that is sleeping is interrupted
     */
    public boolean sendEmailToClient(Client c) throws IOException, InterruptedException {
        String nomeficheiro = "emailAndSMSMessages.txt";
        try (PrintWriter out = new PrintWriter(nomeficheiro)) {
            out.println("Welcome to the Application.");
            out.println("You have been sucessfully registered.");
            out.println();
            out.println("Your password to be used in the application is :" + c.getPassword());
        } catch (IOException e) {
            System.out.println("The file has not been created since there was an error. Please try again.");
        }
        File file = new File(nomeficheiro);
        if (file.createNewFile()) {
            System.out.println("Email sent!");
        }

        return true;
    }

    /**
     * Checks whether an object passed by parameter already exists in the database.
     *
     * @param c instance of the class Client
     * @return true if the Client already exists in the ArrayList on the Class ClientList.
     */
    private boolean validateClient(Client c) {
        return !clientList.isClientInList(c);
    }

    /**
     * Saves the Client to the doing verifications beforehand
     *
     * @param c instance of the class Client
     */
    public boolean saveClient(Client c) {
        int i = 0;
        if (validateClient(c)) {
            ClientList cl = this.getClientList();
            cl.saveClient(c);
        } else {
            System.out.println("The client needs to have at least one unique attribute. Please try again.");
            i = 1;
        }
        addClientToSystem(c);
        return i != 1;
    }

    /**
     * Adds the Client instance to the platform, so that he can login with the credentials given.
     *
     * @param c instance of the class Client
     */
    private void addClientToSystem(Client c) {
        App.getInstance().getCompany().getAuthFacade().addUserWithRole(c.getName(), c.getEmail(), c.getPassword(), Constants.ROLE_CLIENT);
    }

    public String generateSimpleNhsReportTestsPerformed(Calendar currentDate, int historicalPoints, Calendar newerRegressionIntervalDate, Calendar olderRegressionIntervalDate, double aParameterSignificance, double aTestParameter, double bParamatereSignificance, double bTestParameter, double fTestSignificance, double confidenceIntervalSignificance) {
        LinearRegression linearRegression = new LinearRegression(testStore.getTestsPerformedPerDay(olderRegressionIntervalDate, newerRegressionIntervalDate), testStore.getPositiveCovidTestsPerformedOnDay(olderRegressionIntervalDate, newerRegressionIntervalDate));
        NHSReport report = new NHSReport(linearRegression, testStore.getPositiveCovidTestsPerformedOnDay(getHistoricalPointDayOlderDate(currentDate, historicalPoints), currentDate), testStore.getTestsPerformedPerDay(getHistoricalPointDayOlderDate(currentDate, historicalPoints), currentDate));
        return report.getReportString(getHistoricalPointDayOlderDate(currentDate, historicalPoints), currentDate, aParameterSignificance, aTestParameter, bParamatereSignificance, bTestParameter, fTestSignificance, confidenceIntervalSignificance, "days");
    }

    public String generateSimpleNhsReportMeanAge(Calendar currentDate, int historicalPoints, Calendar newerRegressionIntervalDate, Calendar olderRegressionIntervalDate, double aParameterSignificance, double aTestParameter, double bParamatereSignificance, double bTestParameter, double fTestSignificance, double confidenceIntervalSignificance) {
        LinearRegression linearRegression = new LinearRegression(testStore.getMeanAgeOfTestsPerformedPerDay(olderRegressionIntervalDate, newerRegressionIntervalDate), testStore.getPositiveCovidTestsPerformedOnDay(olderRegressionIntervalDate, newerRegressionIntervalDate));
        NHSReport report = new NHSReport(linearRegression, testStore.getPositiveCovidTestsPerformedOnDay(getHistoricalPointDayOlderDate(currentDate, historicalPoints), currentDate), testStore.getMeanAgeOfTestsPerformedPerDay(getHistoricalPointDayOlderDate(currentDate, historicalPoints), currentDate));
        return report.getReportString(getHistoricalPointDayOlderDate(currentDate, historicalPoints), currentDate, aParameterSignificance, aTestParameter, bParamatereSignificance, bTestParameter, fTestSignificance, confidenceIntervalSignificance, "days");
    }

    public String getTheBestModelSLR(Calendar currentDate, int historicalPoints, Calendar newerRegressionIntervalDate, Calendar olderRegressionIntervalDate, double aParameterSignificance, double aTestParameter, double bParamatereSignificance, double bTestParameter, double fTestSignificance, double confidenceIntervalSignificance) {
        LinearRegression linearRegressionTestsPerformed = new LinearRegression(testStore.getTestsPerformedPerDay(olderRegressionIntervalDate, newerRegressionIntervalDate), testStore.getPositiveCovidTestsPerformedOnDay(olderRegressionIntervalDate, newerRegressionIntervalDate));
        LinearRegression linearRegressionMeanAge = new LinearRegression(testStore.getMeanAgeOfTestsPerformedPerDay(olderRegressionIntervalDate, newerRegressionIntervalDate), testStore.getPositiveCovidTestsPerformedOnDay(olderRegressionIntervalDate, newerRegressionIntervalDate));
        if (linearRegressionTestsPerformed.R2() > linearRegressionMeanAge.R2()) {
            return "meanAge";
        }
        return "testsPerformed";
    }

    public String generateSimpleNHSReportTestsPerformedWeeks(Calendar currentDate, int historicalPoints, Calendar newerRegressionIntervalDate, Calendar olderRegressionIntervalDate, double aParameterSignificance, double aTestParameter, double bParamatereSignificance, double bTestParameter, double fTestSignificance, double confidenceIntervalSignificance) {
        LinearRegression linearRegression = new LinearRegression((testStore.getTestsPerformedPerWeek(olderRegressionIntervalDate, newerRegressionIntervalDate)), testStore.getTestsPositivePerWeek(olderRegressionIntervalDate, newerRegressionIntervalDate));
        NHSReport report = new NHSReport(linearRegression, testStore.getTestsPositivePerWeek(getHistoricalPointWeekOlderDate(currentDate, historicalPoints), currentDate), testStore.getTestsPerformedPerWeek(getHistoricalPointWeekOlderDate(currentDate, historicalPoints), currentDate));
        return report.getReportString(getHistoricalPointWeekOlderDate(currentDate, historicalPoints), currentDate, aParameterSignificance, aTestParameter, bParamatereSignificance, bTestParameter, fTestSignificance, confidenceIntervalSignificance, "weeks");
    }

    public String generateSimpleNHSReportMeanAgeWeeks(Calendar currentDate, int historicalPoints, Calendar newerRegressionIntervalDate, Calendar olderRegressionIntervalDate, double aParameterSignificance, double aTestParameter, double bParamatereSignificance, double bTestParameter, double fTestSignificance, double confidenceIntervalSignificance) {
        LinearRegression linearRegression = new LinearRegression((testStore.getMeanAgePerWeek(olderRegressionIntervalDate, newerRegressionIntervalDate)), testStore.getTestsPositivePerWeek(olderRegressionIntervalDate, newerRegressionIntervalDate));
        NHSReport report = new NHSReport(linearRegression, testStore.getTestsPositivePerWeek(getHistoricalPointWeekOlderDate(currentDate, historicalPoints), currentDate), testStore.getMeanAgePerWeek(getHistoricalPointWeekOlderDate(currentDate, historicalPoints), currentDate));
        return report.getReportString(getHistoricalPointWeekOlderDate(currentDate, historicalPoints), currentDate, aParameterSignificance, aTestParameter, bParamatereSignificance, bTestParameter, fTestSignificance, confidenceIntervalSignificance, "weeks");
    }

    public String generateMultilinearNHSReportDays(Calendar currentDate, int historicalPoints, Calendar newerRegressionIntervalDate, Calendar olderRegressionIntervalDate, double b0ParameterSignificance, double b1ParamaterSignificance, double b2ParameterSignificance, double fTestSignificance, double confidenceIntervalSignificance){
MultiLinearRegression multiLinearRegression= new MultiLinearRegression(testStore.getTestsPerformedPerDay(olderRegressionIntervalDate,newerRegressionIntervalDate),testStore.getMeanAgeOfTestsPerformedPerDay(olderRegressionIntervalDate,newerRegressionIntervalDate),testStore.getPositiveCovidTestsPerformedOnDay(olderRegressionIntervalDate,newerRegressionIntervalDate));
NHSReport report= new NHSReport(multiLinearRegression,testStore.getTestsPerformedPerDay(getHistoricalPointDayOlderDate(currentDate,historicalPoints),currentDate),testStore.getMeanAgeOfTestsPerformedPerDay(getHistoricalPointDayOlderDate(currentDate,historicalPoints),currentDate),testStore.getPositiveCovidTestsPerformedOnDay(getHistoricalPointDayOlderDate(currentDate,historicalPoints),currentDate));
return report.getReportString(getHistoricalPointDayOlderDate(currentDate,historicalPoints),currentDate,b0ParameterSignificance,b1ParamaterSignificance,b2ParameterSignificance,0,fTestSignificance,confidenceIntervalSignificance,"days");
}

public String generateMultilinearNHSReportWeeks(Calendar currentDate, int historicalPoints, Calendar newerRegressionIntervalDate, Calendar olderRegressionIntervalDate, double b0ParameterSignificance, double b1ParamaterSignificance, double b2ParameterSignificance, double fTestSignificance, double confidenceIntervalSignificance){
        MultiLinearRegression multiLinearRegression = new MultiLinearRegression(testStore.getTestsPerformedPerWeek(olderRegressionIntervalDate,newerRegressionIntervalDate),testStore.getMeanAgePerWeek(olderRegressionIntervalDate,newerRegressionIntervalDate),testStore.getTestsPositivePerWeek(olderRegressionIntervalDate,newerRegressionIntervalDate));
        NHSReport report = new NHSReport(multiLinearRegression,testStore.getTestsPerformedPerWeek(getHistoricalPointWeekOlderDate(currentDate,historicalPoints),currentDate),testStore.getMeanAgePerWeek(getHistoricalPointWeekOlderDate(currentDate,historicalPoints),currentDate),testStore.getTestsPositivePerWeek(getHistoricalPointWeekOlderDate(currentDate,historicalPoints),currentDate));
        return report.getReportString(getHistoricalPointWeekOlderDate(currentDate,historicalPoints),currentDate,b0ParameterSignificance,b1ParamaterSignificance,b2ParameterSignificance,0,fTestSignificance,confidenceIntervalSignificance,"weeks");

}
    private Calendar getHistoricalPointDayOlderDate(Calendar currentDate, int historicalPointAmount) {
        int i = historicalPointAmount;
        Calendar olderDate = Calendar.getInstance();
        Calendar currentDateUsed = (Calendar) currentDate.clone();
        currentDateUsed.add(Calendar.DAY_OF_MONTH, -1);
        olderDate.set(currentDateUsed.get(Calendar.YEAR), currentDateUsed.get(Calendar.MONTH), currentDateUsed.get(Calendar.DAY_OF_MONTH));
        do {
            if (olderDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                olderDate.add(Calendar.DAY_OF_MONTH, -2);
            } else {
                olderDate.add(Calendar.DAY_OF_MONTH, -1);

            }
            i--;
        } while (i != 0);
        return (Calendar) olderDate.clone();
    }
    public void sendReportToNHS(String report){
        Report2NHS.writeUsingFileWriter(report);
    }

    private Calendar getHistoricalPointWeekOlderDate(Calendar currentDate, int historicalPointAmount) {
        int i = historicalPointAmount;
        Calendar olderDate = Calendar.getInstance();
        Calendar currentDateUsed = (Calendar) currentDate.clone();
        olderDate.set(currentDateUsed.get(Calendar.YEAR), currentDateUsed.get(Calendar.MONTH), currentDateUsed.get(Calendar.DAY_OF_MONTH));
        olderDate.add(Calendar.WEEK_OF_YEAR, -i);
        return (Calendar) olderDate.clone();
    }


}
