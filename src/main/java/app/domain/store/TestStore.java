package app.domain.store;

import app.controller.App;
import app.domain.model.*;

import java.io.Serializable;
import java.time.*;
import java.util.*;

public class TestStore implements Serializable {
    /**
     * Object Oriented class used to store the Tests.
     */
    List<Test> tests;
    TestParam testParam;
    Parameter parameter;
    TestResult testResult;

    /**
     * Test Store constructor.
     */
    public TestStore() {
        tests = new ArrayList<>();
    }

    public TestStore(List<Test> tests) {
        this.tests = tests;
    }

    public List<Test> getTestsWithoutDiagnosis() {
        List<Test> complete = new ArrayList<>();
        for (Test t : this.tests) {
            if (t.getSamplesCollectionDate() != null && t.getChemicalAnalysisDate() != null && t.getDiagnosisDate() == null) {
                complete.add(t);
            }
        }
        return complete;
    }


    /**
     * Checks the TestStore's Test List to get all the UnvalidatedTests.
     *
     * @return- List with all of the Unvalidated tests.
     */
    public List<Test> getUnvalidatedTests() {
        List<Test> lUnvalidatedTests = new ArrayList<>();
        for (int i = 0; i < tests.size(); i++) {
            if (isUnvalidatedTest(tests.get(i))) {
                lUnvalidatedTests.add(tests.get(i));
            }
        }
        return lUnvalidatedTests;
    }

    /**
     * Getter for all the parameters that have passed through the automatic validation done by the API
     *
     * @param parameterCode parameter where it is going to be validated
     * @param barcode       barcode of a sample of the test
     * @return a list containing a list of parameters that passed sucessfully through the automatic validation
     */

    public List<Parameter> getValidatedParameters(String parameterCode, String barcode) {

        Test test = getTestByBarcode(barcode);
        List<Parameter> parameters = test.getParameter();
        Parameter parameterFromWhichTestResultWillBeExtracted = testParam.findParameterInTestParameter(parameterCode, parameters);
        TestResult testResultBeingValidated = parameterFromWhichTestResultWillBeExtracted.getTestResult();

        if (isTestResultInBetweenReferenceValue(testResultBeingValidated) && parameter != null) {
            validatedParameterList.add(parameterFromWhichTestResultWillBeExtracted);
        }

        return validatedParameterList;

    }


    /**
     * Getter for the tests that have at least one sample
     *
     * @return a list containg only tests that have samples
     */

    public List<Test> getTestsWithSamples() {
        List<Test> result = new ArrayList<>();
        for (Test t : this.tests) {
            if (t.getSamplesCollectionDate() != null) {
                result.addAll(this.tests);
            }
        }
        return this.tests;
    }

    public Test getTestByInternalCode(String testCode) {
        for (Test t : this.tests) {
            if (t.getInternalCode().equals(testCode)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Getter for the parameters associated to a test
     *
     * @param barcode barcode of a sample that is contained on a test
     * @return list of parameters of a test
     */
    public List<Parameter> getParameterTestToShow(String barcode) {
        Test test = getTestByBarcode(barcode);
        return test.getParameterStoreToShow();
    }

    /**
     * Getter for a test that has a sample associated to that barcode
     *
     * @param barcode barcode of a sample that is contained on a test
     * @return test if it was found
     */

    public Test getTest(String barcode) {

        for (Test testFound : tests) {
            if (testExists(barcode)) {
                return testFound;
            }
        }
        return null;
    }


    /**
     * Setter for the testParam
     *
     * @param testParam an instance of the class testParam
     */
    public void setTestParam(TestParam testParam) {
        this.testParam = testParam;
    }


    List<Parameter> validatedParameterList;

    public void initializeValidationList() {
        validatedParameterList = new ArrayList<>();
    }

    public TestResult addTestResult(String parameterCode, double result, Test test) {
        testParam = new TestParam(test);

        if (testParam.findParameterInTestParameter(parameterCode) != null) {
            parameter = testParam.findParameterInTestParameter(parameterCode);
            testResult = test.addTestResult(parameter, result);
            return testResult;
        }
        return null;
    }

    /**
     * Creates an object of Test class.
     *
     * @param nhsCode           - NHS code of the test.
     * @param internalCode      - Internal code of the test.
     * @param client            - Client that performs the test.
     * @param testType          - Test Type of the test.
     * @param parameterCategory - Category/Categories of the test.
     * @param parameter         - Parameter/Parameters of the test.
     * @param registrationDate  - Test registration date.
     * @return Object of Test class.
     */
    public Test createTest(String nhsCode, String internalCode, Client client, TestType testType, List<ParameterCategory>
            parameterCategory, List<Parameter> parameter, Calendar registrationDate) {

        return new Test(nhsCode, internalCode, client, testType,
                parameterCategory, parameter, registrationDate);
    }

    /**
     * Saves a test.
     *
     * @param t - Test.
     */
    public void saveTest(Test t) {
        validateTest(t);
        addTest(t);
    }

    /**
     * Validates a test.
     *
     * @param t - Test.
     */
    private void validateTest(Test t) {
        checkNhsCode(t.getNhsCode());
    }

    /**
     * Checks Test NHS Code rules.
     *
     * @param nhsCode - NHS Code of the test.
     */
    private void checkNhsCode(String nhsCode) {
        if (!nhsCode.matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("Code must be alphanumeric.");
        }
        if (nhsCode.length() != 12)
            throw new IllegalArgumentException("Code must have 12 chars.");
    }

    /**
     * Adds a test to the list.
     *
     * @param t - Test.
     */
    private void addTest(Test t) {
        this.tests.add(t);
    }

    /**
     * Method to get all tests.
     *
     * @return - all test stored in list.
     */
    public List<Test> getTests() {
        List<Test> testList = new ArrayList<>();
        testList.addAll(this.tests);
        return testList;
    }

    public List<Test> getTestsFromClient(Client client) {
        List<Test> testListFromTargetClient = new ArrayList<>();
        for (Test test : tests) {
            if (test.getClient().equals(client)) {
                testListFromTargetClient.add(test);
            }
        }
        return testListFromTargetClient;
    }

    public List<Test> getValidatedTestsFromClient(Client client) {
        List<Test> testListFromTargetClient = new ArrayList<>();
        for (Test test : tests) {
            if (test.getClient().equals(client) && isValidatedTest(test)) {
                testListFromTargetClient.add(test);
            }
        }
        return testListFromTargetClient;
    }


    public List<Client> getClientsThatHaveAtLeastOneTestValidated() {
        List<Client> clients = App.getInstance().getCompany().getClientList().getClients();
        List<Client> clientsThatHaveAtLeastOneTestValidated = new ArrayList<>();
        for (Test test : tests) {
            for (Client client1 : clients) {
                if (test.getClient() == client1 && test.getValidationDate() != null) {
                    clientsThatHaveAtLeastOneTestValidated.add(client1);
                }
            }
        }
        return clientsThatHaveAtLeastOneTestValidated;
    }


    public List<Test> getTestsWithoutSamples() {
        List<Test> result = new ArrayList<>();
        for (Test t : this.tests) {
            if (t.getLabID().equals(App.getInstance().getCompany().getCLA().getLaboratoryID())) {
                if (t.getSamplesCollectionDate() == null) {
                    result.add(t);
                }
            }
        }
        return result;
    }


    /**
     * Seeks for a test containing a sample that as associated to itself the given barcode
     *
     * @param barcode barcode of a sample that is contained on a test
     * @return the test if it found it
     */

    public Test getTestByBarcode(String barcode) {
        for (Test t : this.tests) {
            for (Sample samplesOfATest : t.getSamples()) {
                if (samplesOfATest.getBarcode().equals(barcode)) {
                    return t;
                }
            }
        }
        return null;
    }


    public Sample createSample(String barcode) {
        return new Sample(barcode);
    }

    public void saveSample(Test t, Sample s) {
        validateSample(s);
        addSample(t, s);
    }

    private void validateSample(Sample s) {
        if (!s.getBarcode().matches("[0-9]+")) {
            throw new IllegalArgumentException("Barcode must be numeric.");
        }
        if (s.getBarcode().length() != 11) {
            throw new IllegalArgumentException("Barcode must have 11 chars.");
        }
        Company cmp = App.getInstance().getCompany();
        List<Test> tests = cmp.getAllTest();
        for (Test test : tests) {
            for (Sample sample : test.getSamples()) {
                if (s.getBarcode().equals(sample.getBarcode())) {
                    throw new IllegalArgumentException("Barcode must be unique");
                }
            }
        }
    }

    private void addSample(Test t, Sample s) {
        t.getSamples().add(s);
        t.setSamplesCollectionDate(Calendar.getInstance());
    }

    /**
     * Checks if the test associated to a given barcode exists
     *
     * @param barcode barcode of a sample that is contained on a test
     * @return boolean asserting if it found the test with that barcode associated or not
     */

    public boolean testExists(String barcode) {
        for (Test t : this.tests) {
            for (Sample samplesOfATest : t.getSamples()) {
                if (samplesOfATest.getBarcode().equals(barcode)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * See if a given test has passed through the medical lab technician already
     *
     * @param barcode barcode of a sample that is contained on a test
     * @return boolean asserting if it has passed through the medical lab technician or not
     */
    public boolean hasTestPassedSampleCollection(String barcode) {
        return getTest(barcode).getSamplesCollectionDate() != null;
    }


    /**
     * Checks whether the parameter has passed through the automatic validation test
     *
     * @param parameterBeingTested parameter where it is validated
     * @return boolean containing the result on whether the result is between the reference values or not
     */
    public boolean isTestResultInBetweenReferenceValue(TestResult parameterBeingTested) {
        double maxValue = parameterBeingTested.getReferenceValue().getMaxValue();
        double minValue = parameterBeingTested.getReferenceValue().getMinValue();
        double result = parameterBeingTested.getResult();
        return result > minValue && result < maxValue;

    }


    /**
     * Save the test result of the test
     *
     * @param parameterCode parameter code from where the test result will be associated to the parameter
     * @param barcode       barcode from a sample of the test
     */


    public void saveTestResult(String parameterCode, String barcode) {
        Test test = getTestByBarcode(barcode);
        List<Parameter> parameters = test.getParameter();
        parameter = testParam.findParameterInTestParameter(parameterCode, parameters);
        if (parameter != null) {
            parameter.setTestResult(testResult);
        }
    }


    /**
     * Method used to check if a test is ready to be used by the Laboratory Coordinator. Checks if the
     * registrationDate, the sampleCollectionDate, the chemicalAnalysisDate and the diagnosisDate aren't null,while the validationDate is.
     *
     * @param t- Test Object to be checked
     * @return - boolean value saying if the Object is ready to be used or not.
     */
    private boolean isUnvalidatedTest(Test t) {
        return t.getRegistrationDate() != null && t.getSamplesCollectionDate() != null && t.getChemicalAnalysisDate() != null && t.getDiagnosisDate() != null && t.getValidationDate() == null;
    }

    /**
     * Method used to check if a test is validated. Checks if the
     * registrationDate, the sampleCollectionDate, the chemicalAnalysisDate, the diagnosisDate and validationDate aren't null.
     *
     * @param t- Test Object to be checked
     * @return - boolean value saying if the Object is ready to be used or not.
     */
    private boolean isValidatedTest(Test t) {
        return t.getRegistrationDate() != null && t.getSamplesCollectionDate() != null && t.getChemicalAnalysisDate() != null && t.getDiagnosisDate() != null && t.getValidationDate() != null;
    }

    /**
     * Method used to change the validationDate of a List of Test Objects.
     *
     * @param lTests  -  List to be changed.
     * @param newDate - Date used for the validationDate.
     */
    public void validateTests(List<Test> lTests, Calendar newDate) {
        for (int i = 0; i < lTests.size(); i++) {
            lTests.get(i).setValidationDate(newDate);
            try {
                lTests.get(i).sendTestCompletedNotification();
            } catch (Exception e) {

            }

        }
    }
    private List<Test> getValidatedTests(){
        List<Test> resultList=new ArrayList<>();
        for (int i=0;i<tests.size();i++){
            if (tests.get(i).getValidationDate()!=null&&tests.get(i).getTestType().getDescription().equals("Covid")){
                resultList.add(tests.get(i));
            }
        }
        return resultList;
    }

    /**
     * Method to validate the imported tests and prevent equals nhsCodes.
     *
     * @param t - Test.
     * @return - true if the test is not valid.
     */
    public boolean validateImportedTests(Test t) {
        boolean validation = false;
        List<Test> testList = new ArrayList<>(this.tests);
        for (Test test : testList) {
            if (test.getNhsCode().equals(t.getNhsCode())) {
                validation = true;
            }
        }
        return validation;
    }

    /**
     * Creates a list with all the validated tests in a certain date interval
     * @param olderDate-older date to be used for the interval
     * @param newerDate-newer date to be used for the interval
     * @return List with all the tests in the interval.
     */
   private List<Test> getTestsInDayInterval(Calendar olderDate, Calendar newerDate) {
        List<Test> lTestsInInterval = new ArrayList<>();
        Calendar olderDateUsed= (Calendar)olderDate.clone();
        Calendar newerDateUsed= (Calendar)newerDate.clone();
        olderDateUsed.add(Calendar.DAY_OF_MONTH,-1);
        newerDateUsed.add(Calendar.DAY_OF_MONTH,1);
        for (int i = 0; i < getValidatedTests().size(); i++) {
            if (getValidatedTests().get(i).getValidationDate().before(newerDateUsed)&& getValidatedTests().get(i).getValidationDate().after(olderDateUsed)) {
                lTestsInInterval.add(getValidatedTests().get(i));
            }
        }
        return lTestsInInterval;
    }

    /**
     * Creates a list with all the validated tests in a certain date interval(only accepts Mondays).
     * @param olderDate-older date to be used for the interval
     * @param newerDate-newer date to be used for the interval
     * @return List with all the tests in the interval.
     */
    private List<Test> getTestsInWeekListInterval(Calendar olderDate,Calendar newerDate) {
        if (olderDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY || olderDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            throw new IllegalArgumentException("The week day can only be monday.");
        }
        List<Test> lTestsInInterval = new ArrayList<>();
        Calendar olderDateUsed = (Calendar) olderDate.clone();
        Calendar newerDateUsed = (Calendar) newerDate.clone();
        olderDateUsed.add(Calendar.DAY_OF_MONTH, -1);
        newerDateUsed.add(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < getValidatedTests().size(); i++) {
            if (getValidatedTests().get(i).getValidationDate().before(newerDateUsed) && getValidatedTests().get(i).getValidationDate().after(olderDateUsed)) {
                lTestsInInterval.add(getValidatedTests().get(i));
            }
        }
        return lTestsInInterval;
    }

    /**
     * returns a List with all the days in a date interval(except for sundays)
     * @param olderDate-older date to be used for the interval
     * @param newerDate-newer date to be used for the interval
     * @return a List with all the dates
     */
   private List<Calendar> getTotalDateList(Calendar olderDate,Calendar newerDate){
       Calendar olderDateUsed=(Calendar)olderDate.clone();
       Calendar newerDateUsed=(Calendar)newerDate.clone();
       List<Calendar> totalDates = new ArrayList<>();
       newerDateUsed.add(Calendar.DAY_OF_MONTH,1);
       do {
           Calendar tempDateUsed=(Calendar) olderDateUsed.clone();
           if (tempDateUsed.get(Calendar.DAY_OF_WEEK)!= Calendar.SUNDAY){
               totalDates.add(tempDateUsed);
           }
           olderDateUsed.add(Calendar.DAY_OF_MONTH,1);
       }while (olderDateUsed.before(newerDateUsed));
       return totalDates;
   }

    /**
     * retuns a list with all the mondays in an interval
     * @param olderDate-starting monday
     * @param newerDate-finishing monday
     * @return- a list with dates( only mondays)
     */
    private   List<Calendar> getTotalWeekList(Calendar olderDate,Calendar newerDate){
        Calendar olderDateUsed=(Calendar)olderDate.clone();
        Calendar newerDateUsed=(Calendar)newerDate.clone();
        List<Calendar> totalDates = new ArrayList<>();
        do {
            Calendar tempDateUsed=(Calendar) olderDateUsed.clone();
            if (tempDateUsed.get(Calendar.DAY_OF_WEEK)== Calendar.MONDAY){
                totalDates.add(tempDateUsed);
            }
            olderDateUsed.add(Calendar.DAY_OF_MONTH,1);
        }while (olderDateUsed.before(newerDateUsed));
        return totalDates;
    }

    /**
     * returns an array with doubles corresponding to the tests performed in the day of the interval
     * @param olderDate-starting date.
     * @param newerDate-ending date.
     * @return- array with the number of tests per day in a certain interval(except sundays).
     */
    public double[] getTestsPerformedPerDay(Calendar olderDate,Calendar newerDate){
        int cont=0;
        Calendar olderDateUsed=(Calendar)olderDate.clone();
        Calendar newerDateUsed=(Calendar)newerDate.clone();
        List<Test> lTestsInInterval =getTestsInDayInterval(olderDateUsed,newerDateUsed);
        List<Calendar> totalDates = getTotalDateList(olderDateUsed,newerDateUsed);
        double[] arrDouble= new double[totalDates.size()];
        for (int i =0;i<arrDouble.length;i++){
            Calendar dayDate= totalDates.get(i);
            for (Test test:lTestsInInterval
                 ) {
                Calendar testDate= test.getValidationDate();

                if (testDate.get(Calendar.YEAR)==dayDate.get(Calendar.YEAR)&&testDate.get(Calendar.MONTH)==dayDate.get(Calendar.MONTH)&&testDate.get(Calendar.DAY_OF_MONTH)==dayDate.get(Calendar.DAY_OF_MONTH)){
                   cont++;
                }
            }
            arrDouble[i]=cont;
            cont=0;
        }
        return arrDouble;
    };

    /**
     * returns an array with doubles corresponding to the tests performed in the weeks of the interval
     * @param olderDate-starting date.
     * @param newerDate-ending date.
     * @return- array with the number of tests per week in a certain interval.
     */
        public double[] getTestsPerformedPerWeek(Calendar olderDate,Calendar newerDate){
            int cont=0;
            Calendar olderDateUsed=(Calendar)olderDate.clone();
            Calendar newerDateUsed=(Calendar)newerDate.clone();
            List<Test> lTestsInInterval =getTestsInWeekListInterval(olderDateUsed,newerDateUsed);
            List<Calendar> totalDates = getTotalWeekList(olderDateUsed,newerDateUsed);
            double[] arrDouble= new double[totalDates.size()];
            for (int i =0;i<arrDouble.length;i++){
                Calendar dayDate= totalDates.get(i);
                for (Test test:lTestsInInterval
                ) {
                    Calendar testDate= test.getValidationDate();

                    if (testDate.get(Calendar.YEAR)==dayDate.get(Calendar.YEAR)&&testDate.get(Calendar.MONTH)==dayDate.get(Calendar.MONTH)&&testDate.get(Calendar.WEEK_OF_YEAR)==dayDate.get(Calendar.WEEK_OF_YEAR)){
                        cont++;
                    }
                }
                arrDouble[i]=cont;
                cont=0;
            }
            return arrDouble;
   }

    /**
     * returns an array with doubles corresponding to the positive covid tests performed in the weeks of the interval
     * @param olderDate-starting date.
     * @param newerDate-ending date.
     * @return- array with the number of positive covidtests per week in a certain interval.
     */
    public double[] getTestsPositivePerWeek(Calendar olderDate,Calendar newerDate){
        int cont=0;
        Calendar olderDateUsed=(Calendar)olderDate.clone();
        Calendar newerDateUsed=(Calendar)newerDate.clone();
        List<Test> lTestsInInterval =getTestsInWeekListInterval(olderDateUsed,newerDateUsed);
        List<Calendar> totalDates = getTotalWeekList(olderDateUsed,newerDateUsed);
        double[] arrDouble= new double[totalDates.size()];
        for (int i =0;i<arrDouble.length;i++){
            Calendar dayDate= totalDates.get(i);
            for (Test test:lTestsInInterval
            ) {
                Calendar testDate= test.getValidationDate();

                if (testDate.get(Calendar.YEAR)==dayDate.get(Calendar.YEAR)&&testDate.get(Calendar.MONTH)==dayDate.get(Calendar.MONTH)&&testDate.get(Calendar.WEEK_OF_YEAR)==dayDate.get(Calendar.WEEK_OF_YEAR)&& test.getTestType().getDescription().equals("Covid")&&test.getParameter().get(0).getTestResult().getResult()>1.4){
                    cont++;
                }
            }
            arrDouble[i]=cont;
            cont=0;
        }
        return arrDouble;

    }

    /**
     * returns an array with doubles corresponding to the mean age tests performed in the weeks of the interval
     * @param olderDate-starting date.
     * @param newerDate-ending date.
     * @return- array with the number of the mean age per week in a certain interval.
     */
    public double[] getMeanAgePerWeek(Calendar olderDate,Calendar newerDate){
        double total=0;
        int cont=0;
        Calendar olderDateUsed=(Calendar)olderDate.clone();
        Calendar newerDateUsed=(Calendar)newerDate.clone();
        List<Test> lTestsInInterval =getTestsInWeekListInterval(olderDateUsed,newerDateUsed);
        List<Calendar> totalDates = getTotalWeekList(olderDateUsed,newerDateUsed);
        double[] arrDouble= new double[totalDates.size()];
        for (int i =0;i<arrDouble.length;i++){
            Calendar dayDate= totalDates.get(i);
            for (Test test:lTestsInInterval
            ) {
                Calendar testDate= test.getValidationDate();
                if (testDate.get(Calendar.YEAR)==dayDate.get(Calendar.YEAR)&&testDate.get(Calendar.MONTH)==dayDate.get(Calendar.MONTH)&&testDate.get(Calendar.WEEK_OF_YEAR)==dayDate.get(Calendar.WEEK_OF_YEAR)){
                    cont++;
                    total+=test.calculateAge();
                }
            }
if (cont==0){
    cont=1;
}
                arrDouble[i] = total / cont;
                cont = 0;
                total=0;

        }
        return arrDouble;
    }


    /**
     * returns an array with doubles corresponding to the positive covid tests performed in the day of the interval
     * @param olderDate-starting date.
     * @param newerDate-ending date.
     * @return- array with the number of positive tests per day in a certain interval(except sundays).
     */
    public double[] getPositiveCovidTestsPerformedOnDay(Calendar olderDate,Calendar newerDate){
        int cont=0;
        Calendar olderDateUsed=(Calendar)olderDate.clone();
        Calendar newerDateUsed=(Calendar)newerDate.clone();
        List<Test> lTestsInInterval =getTestsInDayInterval(olderDateUsed,newerDateUsed);
        List<Calendar> totalDates = getTotalDateList(olderDateUsed,newerDateUsed);
        double[] arrDouble= new double[totalDates.size()];
            for (int i =0;i<arrDouble.length;i++){
                Calendar dayDate= totalDates.get(i);
                for (Test test:lTestsInInterval
                ) {
                    Calendar testDate= test.getValidationDate();

                    if (testDate.get(Calendar.YEAR)==dayDate.get(Calendar.YEAR)&&testDate.get(Calendar.MONTH)==dayDate.get(Calendar.MONTH)&&testDate.get(Calendar.DAY_OF_MONTH)==dayDate.get(Calendar.DAY_OF_MONTH)&& test.getTestType().getDescription().equals("Covid")&&test.getParameter().get(0).getTestResult().getResult()>1.4){
                        cont++;
                    }
                }
            arrDouble[i]=cont;
            cont=0;
        }
        return arrDouble;
    }
    /**
     * returns an array with doubles corresponding to the mean age of thetests performed in the day of the interval
     * @param olderDate-starting date.
     * @param newerDate-ending date.
     * @return- array with the number of the mean age of the tests per day in a certain interval(except sundays).
     */
    public double[] getMeanAgeOfTestsPerformedPerDay(Calendar olderDate,Calendar newerDate){
        int cont=0;
        double totalAge=0;
        Calendar olderDateUsed=(Calendar)olderDate.clone();
        Calendar newerDateUsed=(Calendar)newerDate.clone();
        List<Test> lTestsInInterval =getTestsInDayInterval(olderDateUsed,newerDateUsed);
        List<Calendar> totalDates = getTotalDateList(olderDateUsed,newerDateUsed);
        double[] arrDouble= new double[totalDates.size()];
        for (int i =0;i<arrDouble.length;i++){
            Calendar dayDate= totalDates.get(i);
            for (Test test:lTestsInInterval
            ) {
                Calendar testDate= test.getValidationDate();

                if (testDate.get(Calendar.YEAR)==dayDate.get(Calendar.YEAR)&&testDate.get(Calendar.MONTH)==dayDate.get(Calendar.MONTH)&&testDate.get(Calendar.DAY_OF_MONTH)==dayDate.get(Calendar.DAY_OF_MONTH)){
                    totalAge+=test.calculateAge();
                    cont++;

                }
            }
            if (cont==0){
                cont=1;
            }
            arrDouble[i]=totalAge/cont;
            cont=0;
            totalAge=0;
        }
        return arrDouble;
    }

}
