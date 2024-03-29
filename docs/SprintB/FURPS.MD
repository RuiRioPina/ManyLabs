# Supplementary Specification (FURPS+)

## Functionality

____

The application has to have the possibility to send emails/sms. Regarding this matter, registered users, on the act of the registration must receive an email stating that the registration was completed and it shall contain the password of the user. Additionally, when the test results are completed the customer shall be notified about it. Now elaborating about more about the localization, the application is to be used in England and therefore it should be in English. With that in mind, since Many Labs has the exclusivity of the COVID-19 tests in England, the company is required by the NHS to report all Covid-19 related information, daily. This reports need to be generated automatically by the application. Finally, there are some security issues that should be addressed such as the clients only being able to access their Test Results and the Specialist Doctor being the only one allowed to access all client data.

## Usability 

____

In terms of aesthetics and design, the client hasn't any clear restriction for this. He stated that he wants to be impressed with it.

About the help and documentation, the accuracy of the prediction models should be analysed and documented in the application user manual (in the annexes) that must be delivered.

## Reliability
____
The company needs to: identify the number of Covid-19 tests performed, identify all positive results to Covid-19 tests, report the total number of Covid-19 cases per day, per week and per month of the year, and send the forecasts for these same time horizons (the number of Covid-19 cases for the following day, next week and next month)." Our application should be able to predict the number of covid-19 cases in the future.Also :"To make the predictions, the NHS contract defines that a linear regression algorithm should be used. The NHS required that both simple linear and multiple linear regression algorithms should be evaluated to select the best model. The accuracy of the prediction models should be analysed and documented in the application user manual (in the annexes) that must be delivered with the application.". Our software must use the most correct use of linear regression. Also the accuracy will have to be documented and analysed.	


## Performance
__________________
According to the project, in terms of performance the app must be with interfaces that between a user and the system shall have a maximum response time of 3 seconds. The system also should start up in less than 10 seconds. The system should not fail more than 5 days in one year, whenever the system fails, there should be no data loss. The application will be deployed to a machine with 8GB of RAM. The user needs an intermediate CPU usage to support the use of JavaFX and some minor calculations. The application should run on all platforms for which there exists a Java Virtual Machine.


## Supportability
_____
The application must be subjected to various tests to verify its functionality so that, if an error is found, it is resolved.

Unit tests should be implemented, and a coverage report should be generated. 

Mutation tests should also be developed, in order to check if the tests cover all possibilities.

The development team must implement unit tests for all methods except methods that implement Input/Output operations.

To register a client, the receptionist needs the client's citizen card number and personal information( like NHS, TIF and phone number).

Only the specialist doctor is allowed to access all client data. 

To facilitate the access to the results, the application must allow ordering the clients by TIN and by name. 

The application must support the English language only.

The algorithm to be used by the application must be defined through a configuration file.

The implemented algorithm should be analysed in terms of its worst-case time complexity, and it should be compared to a provided benchmark algorithm. 

It is intended that the choice of the ordering algorithm is based on the algorithm complexity.

At least two sorting algorithms should be evaluated and documented in the application user manual that must be delivered with the application.

The application must be developed in Java language using the IntelliJ IDE or Netbeans, and the  graphical interface is to be developed in JavaFX 11.

All the images/figures produced during the software development process should be recorded in SVG format.

## +

### Design Constraints
____

The programming language to be used in this project is Java, as it is refered on the Project Description. We will use the Iterative Incremental Model as the software process. There are some mandatory standards such as using best practices for identifying requirements and for OO software analysis and design. For the development of the project we will use an IDE for coding, in this case IntelliJ Idea. We will use Bitbucket as the version control software and PlantUML for all the UML diagrams.

### Implementation Constraints

____

There should be some criteria to take into account when in the implementation phase. It should be ensured that all the recognized coding standards are adopted (e.g. CamelCase, Classes starting with uppercase letter...); Javadoc should be used to generate useful documentation for Java code. Furthermore, The application must be developed in Java language using the IntelliJ IDE or Netbeans. Moreover, there are certain restricts on data input that should take place and should be verified, such as the phone number having 11 numbers, the tin having 10 numbers, among others... These data input should be verified. The developers should take into account that the machine where the application will be deployed will have 8GB of ram, therefore the application should be designed to not need more than those resources. In the end, the application must run in every Unix operating system, as well as Windows.


### Interface Constraints

____
Our application uses the NHS API to communicate externally, to provide information required by them. The NHS API is used to generate barcodes to identify each sample, what's more it is used on sending to the NHS automatically generated reports,on a daily basis to the NHS.

### Physical Constraints

____
None.
