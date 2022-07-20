package PresentationLayer.JobsCLI.functionalCLI;

import Backend.ServiceLayer.Facades.ServicePerJob.Fanctionalities.SupplierCardFunctionality;
import Backend.ServiceLayer.Result.ErrorResult;
import Backend.ServiceLayer.Result.Result;
import Backend.ServiceLayer.ServiceObjects.Supplier.ServiceContact;
import Obj.Parser;

import java.util.LinkedList;
import java.util.Scanner;

import static Obj.Parser.printResult;

public class SupplierCardManagement {
    private Scanner scanner;
    private SupplierCardFunctionality service;

    public SupplierCardManagement(SupplierCardFunctionality service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void run() {
        String supplierCn = Parser.getStrInput("enter supplier company number:");
        String input = Parser.getStrInput("choose action:\n" +
                "0 : back\n" +
                "1 : get supplier card info\n" +
                "2 : edit supplier card info");
        switch (input) {
            case "0":
                break;
            case "1":
                getSupplierCardInfo(supplierCn);
                break;

            case "2":
                updateSupplierCard(supplierCn);
                break;

            default:
                printInvalidInput();
        }
    }

    private void printInvalidInput() {
        Parser.printResult(new ErrorResult("invalid input"), (v) -> "");
    }


    private void updateSupplierCard(String supplierCn) {
        printUpdateSupplierCard();
        String userInput = Parser.getStrInput("");
        switch (userInput) {
            case "0":
                break;
            case "1":
                editSupplierCn(supplierCn);
                break;
            case "2":
                editSupplierBankAccountNum(supplierCn);
                break;
            case "3":
                editSupplierPaymentMethod(supplierCn);
                break;
            case "4":
                editSupplierPaymentFrequency(supplierCn);
                break;
            case "5":
                editSupplierContacts(supplierCn);
                break;
            case "6":
                editSupplierName(supplierCn);
                break;
            default:
                printInvalidInput();
                break;
        }
    }

    private void editSupplierContacts(String supplierCn) {
        System.out.println("0 : go back");
        System.out.println("1 : add contact");
        System.out.println("2 : remove contact");
        System.out.println("3 : update contact name");
        System.out.println("4 : update contact phone number");
        System.out.println("5 : update contact email");
        String input = Parser.getStrInput();
        switch (input) {
            case "0":
                break;
            case "1":
                addSupplierContact(supplierCn);
                break;
            case "2":
                removeSupplierContact(supplierCn);
                break;
            case "3":
                editSupplierContactName(supplierCn);
                break;
            case "4":
                editSupplierContactPhoneNum(supplierCn);
                break;
            case "5":
                editSupplierContactEmail(supplierCn);
                break;
            default:
                printInvalidInput();
        }
    }

    private void addSupplierContact(String supplierCn) {
        String input = Parser.getStrInput("enter the contact phone number, contact's name and the contact's email");
        String[] parts = input.split(";");
        if (parts.length != 3) {
            printInvalidInput();
            return;
        }
        service.addSupplierContact(supplierCn, parts[0], parts[1], parts[2]);

    }

    private void removeSupplierContact(String supplierCn) {
        String input = Parser.getStrInput("enter the contact phone number");
        String[] parts = input.split(";");
        if (parts.length != 1) {
            printInvalidInput();
            return;
        }
        service.removeSupplierContact(supplierCn, parts[0]);

    }

    private void editSupplierContactPhoneNum(String supplierCn) {
        String input = Parser.getStrInput("enter the contact current phone number, and the new contact phone number");
        String[] parts = input.split(";");
        if (parts.length != 2) {
            printInvalidInput();
            return;
        }
        service.editSupplierContactNumber(supplierCn, parts[0], parts[1]);
    }

    private void editSupplierContactName(String supplierCn) {
        String input = Parser.getStrInput("enter the contact current phone number, and the new name");
        String[] parts = input.split(";");
        if (parts.length != 2) {
            printInvalidInput();
            return;
        }
        service.editSupplierContactName(supplierCn, parts[0], parts[1]);

    }

    private void editSupplierContactEmail(String supplierCn) {
        String input = Parser.getStrInput("enter the contact current phone number, and the new contact email");
        String[] parts = input.split(";");
        if (parts.length != 2) {
            printInvalidInput();
            return;
        }
        service.editSupplierContactEmail(supplierCn, parts[0], parts[1]);
    }


    private void editSupplierCn(String supplierCn) {
        String input = Parser.getStrInput("enter the new supplier cn");
        service.editSupplierCn(supplierCn, input);
    }

    private void editSupplierName(String supplierCn) {
        String input = Parser.getStrInput("enter the new supplier name");
        service.editSupplierName(supplierCn, input);
    }

    private void editSupplierBankAccountNum(String supplierCn) {
        String input = Parser.getStrInput("enter the new bank account number");
        service.editSupplierBankAccountNum(supplierCn, input);
    }

    private void editSupplierPaymentMethod(String supplierCn) {
        String input = Parser.getStrInput("enter the new PaymentMethod");
        service.editSupplierPaymentMethod(supplierCn, input);
    }

    private void editSupplierPaymentFrequency(String supplierCn) {
        String input = Parser.getStrInput("enter the new PaymentFrequency");
        service.editSupplierPaymentFrequency(supplierCn, input);
    }

    private void getSupplierCardInfo(String supplierCn) {
        System.out.print("supplier name:");
        getSupplierName(supplierCn);
        System.out.print("supplier bank account number:");
        getSupplierBankAccountNum(supplierCn);
        System.out.print("supplier payment method:");
        getSupplierPaymentMethod(supplierCn);
        System.out.print("supplier payment frequency:");
        getSupplierPaymentFrequency(supplierCn);
        printGetSupplierCard();
        String input = Parser.getStrInput();
        switch (input) {
            case "0":
                break;
            case "1":
                getSupplierContact(supplierCn);
                break;
            case "2":
                getSupplierContactList(supplierCn);
                break;
            default:
                printInvalidInput();
        }
    }

    private void getSupplierName(String supplierCn) {
        Result<String> result = service.getSupplierName(supplierCn);
        Parser.printResult(result);

    }

    private void getSupplierBankAccountNum(String supplierCn) {
        Result<String> result = service.getSupplierBankAccountNumber(supplierCn);
        Parser.printResult(result);
    }

    private void getSupplierPaymentMethod(String supplierCn) {
        Result<String> result = service.getSupplierPaymentMethod(supplierCn);
        Parser.printResult(result);


    }

    private void getSupplierPaymentFrequency(String supplierCn) {
        Result<String> result = service.getSupplierPaymentFrequency(supplierCn);
        Parser.printResult(result);

    }

    private void getSupplierContact(String supplierCn) {
        String input = Parser.getStrInput("enter the Contact phone number: ");
        Result<ServiceContact> result = service.getSupplierContact(supplierCn, input);
        Parser.printResult(result);
    }

    private void getSupplierContactList(String supplierCn) {
        Result<LinkedList<ServiceContact>> result = service.getSupplierContactList(supplierCn);
        Parser.printResult(result, val -> Parser.printList(val));
    }


    public void printUpdateSupplierCard() {
        System.out.println("choose the action:");
        System.out.println("0 : back");
        System.out.println("1 : update cn");
        System.out.println("2 : update bankAccountNum");
        System.out.println("3 : update paymentMethod");
        System.out.println("4 : update paymentFrequency");
        System.out.println("5 : update contacts");
        System.out.println("6 : update supplier name");
    }

    public void printGetSupplierCard() {
        System.out.println("choose the action:");
        System.out.println("0 : back");
        System.out.println("1 : get contacts");
        System.out.println("2 : get supplier name");
    }
}
