package app.controller;

import app.domain.model.*;
import app.domain.shared.Constants;
import app.domain.store.*;
import auth.AuthFacade;
import auth.UserSession;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Paulo Maio <pam@isep.ipp.pt>
 */
public class App {

    private Company company;
    private AuthFacade authFacade;

    private App() {
        Properties props = getProperties();
        this.company = new Company(props.getProperty(Constants.PARAMS_COMPANY_DESIGNATION));
        this.authFacade = this.company.getAuthFacade();
    }

    public Company getCompany() {
        return this.company;
    }


    public UserSession getCurrentUserSession() {
        return this.authFacade.getCurrentUserSession();
    }

    public boolean doLogin(String email, String pwd) {
        return this.authFacade.doLogin(email, pwd).isLoggedIn();
    }

    public void doLogout() {
        this.authFacade.doLogout();
    }

    private Properties getProperties() {
        Properties props = new Properties();

        // Add default properties and values
        props.setProperty(Constants.PARAMS_COMPANY_DESIGNATION, "Many Labs");


        // Read configured values
        try {
            InputStream in = new FileInputStream(Constants.PARAMS_FILENAME);
            props.load(in);
            in.close();
        } catch (IOException ex) {

        }
        return props;
    }


    private void bootstrap() {


        this.authFacade.addUserRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN);
        this.authFacade.addUserRole(Constants.ROLE_CLIENT, Constants.ROLE_CLIENT);
        this.authFacade.addUserRole(Constants.ROLE_RECEPTIONIST, Constants.ROLE_RECEPTIONIST);
        this.authFacade.addUserRole(Constants.ROLE_CLINICAL_CHEMISTRY_TECHNOLOGIST, Constants.ROLE_CLINICAL_CHEMISTRY_TECHNOLOGIST);
        this.authFacade.addUserRole(Constants.ROLE_MEDICAL_LAB_TECHNICIAN, Constants.ROLE_MEDICAL_LAB_TECHNICIAN);
        this.authFacade.addUserRole(Constants.ROLE_LABORATORY_COORDINATOR, Constants.ROLE_LABORATORY_COORDINATOR);
        this.authFacade.addUserRole(Constants.ROLE_SPECIALIST_DOCTOR, Constants.ROLE_SPECIALIST_DOCTOR);

        this.authFacade.addUserWithRole("Main Administrator", "admin@lei.sem2.pt", "123456", Constants.ROLE_ADMIN);
        this.authFacade.addUserWithRole("Receptionist", "receptionist@lei.sem2.pt", "12", Constants.ROLE_RECEPTIONIST);
        this.authFacade.addUserWithRole("Clinical Chemistry Technologist", "cct@lei.sem2.pt", "12", Constants.ROLE_CLINICAL_CHEMISTRY_TECHNOLOGIST);
        this.authFacade.addUserWithRole("MedicalLabTechnician1", "medlabt@lei.sem2.pt", "123456", Constants.ROLE_MEDICAL_LAB_TECHNICIAN);
        this.authFacade.addUserWithRole("Specialist Doctor", "specd@lei.sem2.pt", "123456", Constants.ROLE_SPECIALIST_DOCTOR);
        this.authFacade.addUserWithRole("LabCoordinator", "labcoord@lab.pt", "123", Constants.ROLE_LABORATORY_COORDINATOR);
        this.authFacade.addUserWithRole("Client", "client@lei.sem2.pt", "123456", Constants.ROLE_CLIENT);
        this.company.setNumberOfEmployees(0);
        this.company.setTestCode(0);

        Sample sample = new Sample("99999999999");
        Sample sample1 = new Sample("11111111111");
        ParameterCategoryStore cs = this.company.getParameterCategoryStore();

        List<ParameterCategory> pc = new ArrayList<>();
        List<ParameterCategory> pc1 = new ArrayList<>();
        List<ParameterCategory> pCovid = new ArrayList<>();
        ParameterCategory p1 = new ParameterCategory("CAT00", "Hemogram");
        ParameterCategory P2 = new ParameterCategory("CAT01", "Category01");
        ParameterCategory p4 = new ParameterCategory("CAT02","Cholestrol");
        ParameterCategory P3 = new ParameterCategory("CAT03", "Covid");
        pc.add(p1);
        pc.add(P2);
        pc1.add(p1);
        pc1.add(p4);
        pCovid.add(P3);

        List<ParameterCategory> p = new ArrayList<>();
        p.add(P3);

        List<ParameterCategory> cat = new ArrayList<>();
        cat.add(pc.get(0));

        List<ParameterCategory> catt = new ArrayList<>();
        catt.add(p4);

        List<ParameterCategory> cat1 = new ArrayList<>();
        cat1.add(P2);

        List<ParameterCategory> cat2 = new ArrayList<>();
        cat2.add(P3);

        cs.saveParameterCategory(p1);
        cs.saveParameterCategory(P2);
        cs.saveParameterCategory(P3);
        cs.saveParameterCategory(p4);
        ParameterStore ps = this.company.getParameterStore();
        ParameterStore psCovid = this.company.getParameterStore();
        psCovid.saveParameter(new Parameter("IgGAN", "IgC", "Antibodies", cat2));
        ps.saveParameter(new Parameter("HDL00","HDL","High Den Lipoprotein",catt));

        ps.saveParameter(new Parameter("HB000", "HB", "Haemoglobin", cat));
        ps.saveParameter(new Parameter("WBC00", "WBC", "White Cell Count", cat));
        ps.saveParameter(new Parameter("PLT00", "PLT", "Platelet Count", cat));
        ps.saveParameter(new Parameter("RBC00", "RBC", "Red Blood Count", cat));
        ps.saveParameter(new Parameter("MCV00", "MCV", "Mean Cell Volume", cat1));
        ps.saveParameter(new Parameter("MCH00", "MCH", "Mean Cell Heaemoglob", cat1));
        ps.saveParameter(new Parameter("MCHC0", "MCHC", "MCH Concentration", cat1));
        ps.saveParameter(new Parameter("ESR00", "ESR", "Erythrocyte Sed Rate", cat1));


        List<Parameter> par = new ArrayList<>();
        List<Parameter> parCovid = new ArrayList<>();
        //par.add(ps.getParameters().get(0));
        par.add(ps.getParameters().get(1));
        par.add(ps.getParameters().get(2));
        par.add(ps.getParameters().get(3));
        par.add(ps.getParameters().get(5));
        parCovid.add(ps.getParameters().get(0));

        TestTypeStore tts = this.company.getTestTypeStore();
        TestType tt1 = new TestType("BLT00", "Blood", "Venipuncture", pc);
        TestType tt3 = new TestType("BLT01","Blood","Venipuncture",pc1);
        TestType tt2 = new TestType("CVD00", "Covid", "Nasopharyngeal", p);
        tts.saveTestType(tt3);
        tts.saveTestType(tt1);
        tts.saveTestType(tt2);

        List <TestType> ttList = new ArrayList<>();
        ttList.add(tt1);
        ttList.add(tt2);

        ClientList cl = this.company.getClientList();
        Client c = new Client(1234567890123456L, 1234567890, "22-01-2002", "jorge@gmail.com", 1111111111L, 22222222222L, "Jorge Ferreira");
        Client c1 = new Client(1234567890123156L, 1234564890, "22-01-2002", "ruipina@gmail.com", 9999999999L, 22221222222L, "Rui Pina");
        cl.saveClient(c);
        cl.saveClient(c1);
        System.out.println(c.getPassword());
        this.authFacade.addUserWithRole(c.getName(), c.getEmail(), c.getPassword(), Constants.ROLE_CLIENT);

        ClinicalAnalysisLaboratoryStore clas = this.company.getClinicalAnalysisLaboratoryStore();
        ClinicalAnalysisLaboratory cla = new ClinicalAnalysisLaboratory("001LN","LABLondon","London",99999979999L,9999999999L,ttList);
        ClinicalAnalysisLaboratory cla2 = new ClinicalAnalysisLaboratory("001MA","ManLAB","Manchester",88888888888L,8888888888L,ttList);
        clas.saveClinicalAnalysisLaboratory(cla);
        clas.saveClinicalAnalysisLaboratory(cla2);
        this.company.setCLA(cla2);


        TestStore ts = this.company.getTestStore();
        Calendar data = Calendar.getInstance();

        
        
        Test testSemSample1 = new Test("111111abcdef", "999999991999", c, tts.getTestTypes().get(0), pc, par, data);

        Test testTestarJavaFX = new Test("111111abcdef", "999999999999", c, tts.getTestTypes().get(0), pc, par, data);
        testTestarJavaFX.setSamplesCollectionDate(data);
        testTestarJavaFX.setChemicalAnalysisDate(data);
        testTestarJavaFX.setDiagnosisDate(data);
        testTestarJavaFX.setValidationDate(data);
        testTestarJavaFX.setReport(new Report("Parece um jovem de 15 anos"));
        Test testSemSample2 = new Test("222222abcdef", "999999999998", c, tts.getTestTypes().get(0), pc, par, data);
        ts.saveTest(testSemSample1);
        ts.saveTest(testSemSample2);
        ts.saveTest(testTestarJavaFX);

        Test test = new Test("333333abcdef", "999999999997", c, tts.getTestTypes().get(0), pc, par, data);
        Test test1 = new Test("444444abcdef", "999999999996", c, tts.getTestTypes().get(0), pc, par, data);

        ts.saveTest(test);
        ts.saveTest(test1);
        ts.saveTest(test);


        ts.saveSample(test, sample);
        ts.saveSample(test1, sample1);


        this.company.setCLA(cla);

        ts = this.company.getTestStore();

        Test testSemSample3 = new Test("666666abcdef", "999999999994", c, tts.getTestTypes().get(0), pc, par, data);
        Test testSemSample4 = new Test("777777abcdef", "999999999993", c, tts.getTestTypes().get(0), pc, par, data);
        ts.saveTest(testSemSample3);
        ts.saveTest(testSemSample4);

        RoleStore lRole = this.company.getRoleStore();
        lRole.add(lRole.create("Receives the client", Constants.ROLE_RECEPTIONIST, "REC"));
        lRole.add(lRole.create("Performs Chemical Analysis and records results", Constants.ROLE_CLINICAL_CHEMISTRY_TECHNOLOGIST, "CCT"));
        lRole.add(lRole.create("Has the responsibility of interacting with the software on a deeper level", Constants.ROLE_MEDICAL_LAB_TECHNICIAN, "MDT"));
        lRole.add(lRole.create("Coordinates the activity on the laboratory", Constants.ROLE_LABORATORY_COORDINATOR, "LC"));
        lRole.add(lRole.create("Responsible for interacting with the client and their tests", Constants.ROLE_SPECIALIST_DOCTOR, "SD"));

    }


    // Extracted from https://www.javaworld.com/article/2073352/core-java/core-java-simply-singleton.html?page=2
    private static App singleton = null;

    public static App getInstance() {
        if (singleton == null) {
            synchronized (App.class) {
                singleton = new App();
                singleton.bootstrap();
            }
        }
        return singleton;
    }

    public void fechar(WindowEvent event) {
        Alert aviso = new Alert(AlertType.CONFIRMATION, "Deseja mesmo sair?", ButtonType.YES, ButtonType.NO);
        aviso.setHeaderText("Confirmação da ação");
        aviso.showAndWait();
        ButtonType resultado = aviso.getResult();
        if(resultado == ButtonType.NO) {
            event.consume();
            return;
        }
    }
}
