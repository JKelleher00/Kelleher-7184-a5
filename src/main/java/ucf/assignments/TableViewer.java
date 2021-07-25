package ucf.assignments;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 John Kelleher
 */
import com.sun.jdi.Value;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class TableViewer {
    public TextField ValueBox;
    public TextField SerialBox;
    public TextField NameBox;
    public TableColumn<ItemObj, String> ValueColumn;
    public TableColumn<ItemObj, String> SerialColumn;
    public TableColumn<ItemObj, String> NameColumn;
    public ObservableList<ItemObj> items = FXCollections.observableArrayList();
    public Button CloseButton;
    public Label ErrorMessage;
    public SubScene popup;
    public TableView Table;
    public ArrayList serials;
    public Button CommitButton;
    public Button AddButton;
    public int index;
    public TextField SaveTitle;
    public TextField LoadTitle;

    public void OnAddClicked(ActionEvent actionEvent) {
        if(SerialBox.getText().length()!=10)
            error("The serial number must be 10 digits.");
        else if(NameBox.getText().length()<2||NameBox.getText().length()>256)
            error("The name should be between 2 and 256 characters in length");
        else{
            int issue = 0;
            if(items.size()>0) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getSerial().equals(SerialBox.getText()))
                        issue = 1;
                }
            }
            if(issue == 1)
                error("The serial number you entered already exists.");
            else {
                ValueColumn.setCellValueFactory(new PropertyValueFactory<ItemObj, String>("Price"));
                SerialColumn.setCellValueFactory(new PropertyValueFactory<ItemObj, String>("Serial"));
                NameColumn.setCellValueFactory(new PropertyValueFactory<ItemObj, String>("Name"));
                ItemObj IO = new ItemObj(ValueBox.getText(), SerialBox.getText(), NameBox.getText());
                items.add(IO);
                Table.setItems(items);
                ValueBox.setText("");
                SerialBox.setText("");
                NameBox.setText("");
            }
        }
    }
    public void error(String ErrorText){
        popup.setVisible(true);
        ErrorMessage.setVisible(true);
        ErrorMessage.setText(ErrorText);
        CloseButton.setVisible(true);
    }

    public void OnClosePressed(ActionEvent actionEvent) {
        popup.setVisible(false);
        ErrorMessage.setVisible(false);
        CloseButton.setVisible(false);
    }

    public void onRemovePressed(ActionEvent actionEvent) {
        ObservableList<ItemObj> selectedRow, allLists;
        allLists = Table.getItems();
        selectedRow = Table.getSelectionModel().getSelectedItems();
        for(ItemObj Io: selectedRow){
            allLists.remove(Io);
        }
        items = Table.getItems();
    }

    public void OnEditPressed(ActionEvent actionEvent) {
        ObservableList<ItemObj> selectedRow;
        selectedRow = Table.getSelectionModel().getSelectedItems();
        for(ItemObj Io: selectedRow){
            AddButton.setVisible(false);
            CommitButton.setVisible(true);
            SerialBox.setText(Io.getSerial());
            NameBox.setText(Io.getName());
            ValueBox.setText(Io.getPrice());
            for(int i = 0;i<items.size();i++){
                if(Io.getSerial().equals(items.get(i).getSerial()))
                    index = i;
            }
            serials.remove(index);
        }
    }

    public void OnCommitPressed(ActionEvent actionEvent) {
        if(SerialBox.getText().length()!=10)
            error("The serial number must be 10 digits.");
        else if(NameBox.getText().length()<2||NameBox.getText().length()>256)
            error("The name should be between 2 and 256 characters in length");
        else {
            int issue = 0;
            if(items.size()>0) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getSerial().equals(SerialBox.getText()))
                        issue = 1;
                }
            }
            if(issue == 1)
                error("The serial number you entered already exists.");
            else {
                ObservableList<ItemObj> AllItems = Table.getItems();
                ObservableList<ItemObj> selectedEdit = Table.getSelectionModel().getSelectedItems();
                AllItems.remove(index);
                ItemObj NIO = new ItemObj(ValueBox.getText(), SerialBox.getText(), NameBox.getText());
                AllItems.add(index, NIO);
                NameBox.setText("");
                SerialBox.setText("");
                ValueBox.setText("");
                CommitButton.setVisible(false);
                AddButton.setVisible(true);
                Table.setItems(AllItems);
                items = Table.getItems();
                serials.add(index,NIO.getSerial());
            }
        }
    }

    public void OnSaveTxtPressed(ActionEvent actionEvent) throws IOException {
        if (SaveTitle.getText().equals(""))
            error("Please enter a title before saving");
        else {
            items = Table.getItems();
            FileWriter fw = new FileWriter(SaveTitle.getText() + ".txt");
            PrintWriter Inventory = new PrintWriter(fw);
            Inventory.println(SaveTitle.getText());
            Inventory.println(items.size());
            for (int i = 0; i < items.size(); i++) {
                Inventory.println(items.get(i).getPrice() + "\t" + items.get(i).getSerial() + "\t" + items.get(i).getName());
            }
            Inventory.close();
            fw.close();
            error("Your inventory has been saved as " + SaveTitle.getText() + ".txt, you can close this window safely");
        }
    }
    public void OnLoadTxtPressed(ActionEvent actionEvent) {
        if(LoadTitle.getText().equals(""))
            error("Please enter a title of a previous list");
        else{
            Path testPath = Paths.get(LoadTitle.getText()+".txt");
            if(Files.exists(testPath)){
                try {
                    ValueColumn.setCellValueFactory(new PropertyValueFactory<ItemObj, String>("Price"));
                    SerialColumn.setCellValueFactory(new PropertyValueFactory<ItemObj, String>("Serial"));
                    NameColumn.setCellValueFactory(new PropertyValueFactory<ItemObj, String>("Name"));
                    Scanner sf = new Scanner(new File(LoadTitle.getText()+".txt"));
                    SaveTitle.setText(sf.nextLine());
                    items.clear();
                    int itemsize = sf.nextInt();
                    for(int i = 0;i<itemsize;i++){
                        ItemObj Io = new ItemObj(sf.next().substring(1),sf.next(),sf.next());
                        items.add(Io);
                    }
                    Table.setItems(items);
                    Table.setEditable(true);
                    } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else error("File not found");
        }
    }

    public void OnSaveHTMLPressed(ActionEvent actionEvent) throws IOException {
        if (SaveTitle.getText().equals(""))
            error("Please enter a title before saving");
        else {
            items = Table.getItems();
            FileWriter fw = new FileWriter(SaveTitle.getText() + ".html");
            PrintWriter Inventory = new PrintWriter(fw);
            Inventory.println("<table style=\"width:100%\">");
            Inventory.println("\t<tr>");
            Inventory.println("\t\t<th>Value</th>");
            Inventory.println("\t\t<th>Serial</th>");
            Inventory.println("\t\t<th>Name</th>");
            Inventory.println("\t</tr>");
            for(int i = 0;i<items.size();i++) {
                Inventory.println("\t<tr>");
                Inventory.println("\t\t<td>"+items.get(i).getPrice()+"</td>");
                Inventory.println("\t\t<td>"+items.get(i).getSerial()+"</td>");
                Inventory.println("\t\t<td>"+items.get(i).getName()+"</td>");
                Inventory.println("\t</tr>");
            }
            Inventory.println("</table>");
            Inventory.close();
            fw.close();
            error("Your inventory has been saved as " + SaveTitle.getText() + ".html, you can close this window safely");
        }
    }
}
