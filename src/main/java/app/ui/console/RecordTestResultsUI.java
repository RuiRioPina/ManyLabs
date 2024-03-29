package app.ui.console;

import app.controller.RecordTestResultsController;

import app.ui.console.utils.Utils;
import app.domain.model.Parameter;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class RecordTestResultsUI implements Runnable {
    RecordTestResultsController recordTestResultsController = new RecordTestResultsController();
    List<Parameter> parametersToShow;
    public RecordTestResultsUI() {
        // Do nothing because there is no need to construct the UI layer with any value. This is only used to be able to use the UI when selecting in menus.

    }


    @Override
    public void run() {


        String barcode;
        double result = 0;
        String parameterCode = "";
        Scanner sc = new Scanner(System.in);
        recordTestResultsController.initializeValidationList();
        Utils.log("\nWelcome to the Record Results of Test Page\n");
        do {
            Utils.log("Please introduce the barcode of the Sample where the results will be recorded.(11 digits)");
            barcode = sc.nextLine();
        } while (!recordTestResultsController.testExists(barcode) || recordTestResultsController.hasTestPassedSampleCollection(barcode));

        parametersToShow = recordTestResultsController.getParameterStoreToShow(barcode);

        while (!parametersToShow.isEmpty()) {

            int option;

            option = Utils.showAndSelectIndex(parametersToShow, "Select parameter to record the result.");
            boolean pass;
            if ((option >= 0) && (option < parametersToShow.size())) {
                do {
                    parameterCode = parametersToShow.get(option).getCode();
                    Utils.log("Please insert the result for the intended parameter: (Metric Used: " +
                            recordTestResultsController.getMetricsFor(parameterCode) + ")");
                    pass = true;
                    try {
                        result = sc.nextDouble();
                    } catch (InputMismatchException e) {
                        Utils.log("The value of the result of the test can only have numbers. Please try again!");
                        pass = false;
                        sc.nextLine();
                    }
                } while (!pass);


                parameterCode = parametersToShow.get(option).getCode();
                recordTestResultsController.addTestResult(parameterCode, result, recordTestResultsController.getTestByBarcode(barcode));
                Utils.log("Confirmation: \n");
                Utils.log(String.format("-Parameter Selected: %s", parameterCode));
                Utils.log(String.format("-Result introduced: %s %s", result, recordTestResultsController.getMetricsFor(parameterCode)));
            }

            saveTestResult(parameterCode,barcode,option);

        }
    }

    private void saveTestResult(String parameterCode, String barcode,int option) {
        try {

            if (!Utils.confirm("Confirm test result creation (y/n)?")) {
                return;

            }
            recordTestResultsController.saveTestResult(parameterCode, barcode);

            parametersToShow.remove(option);

            recordTestResultsController.setChemicalAnalysis(recordTestResultsController.getTestByBarcode(barcode));
        }catch (IndexOutOfBoundsException e) {
            Utils.log("You must insert all the results for all the parameters.");
        }
    }

}

