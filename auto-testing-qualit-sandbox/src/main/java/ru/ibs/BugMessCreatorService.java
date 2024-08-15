package ru.ibs;


import org.openqa.selenium.TimeoutException;

public class BugMessCreatorService {

    public void createBugMessForId(int idCurrentWebElem, int idPreviousProduct, StringBuilder testRes) {
        testRes.append("\n**************************************************************\n")
                .append("The id (#) value is incorrect. \n")
                .append("Expected id value: ")
                .append(idPreviousProduct + 1)
                .append("\n Actual id value: ")
                .append(idCurrentWebElem);
    }

    public void createBugMessForName(String productName, String currentProductName, StringBuilder testRes) {
        testRes.append("\n**************************************************************\n")
                .append("The product name was saved incorrectly. \n")
                .append("Expected product name: ")
                .append(productName)
                .append("\nActual product name: ")
                .append(currentProductName);
    }

    public void createBugMessForProductType(String currentProductType, String productType, StringBuilder testRes) {
        testRes.append("\n**************************************************************\n")
                .append("The value of the product type does not match the selected one.\n")
                .append("Expected result: ")
                .append(currentProductType)
                .append("\nThe actual result: ")
                .append(productType);
    }

    public void createBugMessForExoticCheckbox(boolean exoticCheckboxBool, boolean currentExoticCheckbox,
                                               StringBuilder testRes) {
        testRes.append("\n**************************************************************\n")
                .append("Incorrect value of the exotic checkbox.\n")
                .append("Expected result: ")
                .append(currentExoticCheckbox)
                .append("\nThe actual result: ")
                .append(exoticCheckboxBool);
    }

    public void createOpenModalWindowErrorMessage(StringBuilder testRes, TimeoutException e, int timeToWait){
        testRes.append("The product addition window did not open in the allotted time (seconds): ")
                .append(timeToWait).append("\n")
                .append("Exception message: \n")
                .append(e.getMessage());
    }
}
