/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author youwan
 */
public class FrequencyOfNumberInArray extends Application {

    private final TableView<row> table = new TableView<>();
    private final ObservableList<row> data
            = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        Stage GraphStage = new Stage();

        File input[] = {null};
        HBox root = new HBox();
        Scene scene = new Scene(root, 230, 40);
        stage.setScene(scene);

        HBox GraphRoot = new HBox();
        Scene GraphScene = new Scene(GraphRoot, 1220, 800);
        GraphStage.setScene(GraphScene);

        //plot
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Value");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency");

        ScatterChart scatterChart = new ScatterChart(xAxis, yAxis);
        scatterChart.setPrefSize(1000, 700);

        XYChart.Series dataSeries = new XYChart.Series();

        scatterChart.getData().add(dataSeries);

        //Data Display in TableView
        table.setEditable(true);
        table.setPrefWidth(200);
        TableColumn Value = new TableColumn("Value");
        Value.setPrefWidth(90);
        Value.setCellValueFactory(new PropertyValueFactory<>("Value"));
        
        TableColumn Frequency = new TableColumn("Frequency");
        Frequency.setPrefWidth(90);
        Frequency.setCellValueFactory(new PropertyValueFactory<>("Frequency"));
        table.getColumns().addAll(Value, Frequency);
        table.setItems(data);

        DataDLL dLinkedList = new DataDLL();
        
        
        //Buttons
        Button ComputeButton = new Button("Compute");
        ComputeButton.setDisable(true);
        ComputeButton.setOnAction(event -> {

            FreqDLL FreqList = Frequency(input[0], dLinkedList);
            table.getItems().clear();
            for (int i = 0; i < FreqList.lenght(); i++) {
                dataSeries.getData().add(new XYChart.Data(FreqList.getValue(i), FreqList.getFreq(i)));
                data.add(new row(Integer.toString(FreqList.getValue(i)), Integer.toString(FreqList.getFreq(i))));
            }
        });
        Button InputButton = new Button("Open Input File");
        InputButton.setOnAction(e -> {
            input[0] = InputFileDialog(stage);
            ComputeButton.setDisable(false);
            InputButton.setDisable(true);

            Scanner fileReader = new Scanner(System.in);
            try {
                fileReader = new Scanner(input[0]);
                int value;
                while (fileReader.hasNextLine()) {
                    value = fileReader.nextInt();
                    data.add(new row(Integer.toString(value), null));
                    dLinkedList.addNodeDLL(value);
                }
                fileReader.close();
            } catch (FileNotFoundException ex) { 
                Logger.getLogger(FrequencyOfNumberInArray.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch( InputMismatchException ex){
                Logger.getLogger(FrequencyOfNumberInArray.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(AlertType.ERROR, "Cannot Read " + input[0].toString());
                alert.showAndWait();
                System.exit(1);
            }
        
        });
     
        Button ExitButton = new Button("Quit");
        ExitButton.setOnAction(event -> {
            System.exit(0);            
        });

        root.setSpacing(10);
        root.getChildren().addAll(InputButton, ComputeButton, ExitButton);
        GraphRoot.setSpacing(10);
        GraphRoot.getChildren().addAll(scatterChart, table);

        //SHOW
        GraphStage.setTitle("Scatter Chart");
        GraphStage.show();
        stage.setTitle("GUI");
        stage.show();

    }

    public static class DataDLL {

        class Node {

            public int data;
            public Node previous;
            public Node next;

            public Node(int data) {
                this.data = data;
            }
        }

        //Creation of Head and Tail Node
        Node Head = null;
        Node Tail = null;

        //Add Node to DLL
        public void addNodeDLL(int data) {
            DataDLL.Node newNode = new DataDLL.Node(data);
            if (Head == null) {
                Head = Tail = newNode;
                Head.previous = null;
                Tail.next = null;
            } else {
                Tail.next = newNode;
                newNode.previous = Tail;
                Tail = newNode;
                Tail.next = null;
            }
        }

        //print DLList
        public void printList() {
            if (Head == null) {
                System.out.println("Doubly Linked List is empty");
                return;
            }
            System.out.println("Nodes in Doubly Linked List: ");
            Node currentNode = Head;
            while (currentNode != null) {
                System.out.print(currentNode.data + " ");
                currentNode = currentNode.next;
            }
            System.out.print('\n');
        }
        

        //Sort by ascending the list by Bubble sort
        public void sortList() {
            
            Node current = null;
            Node index = null;  
            int temp; //working variable  
            //Check whether list is empty  
            if(Head == null){
                System.out.println("Double Linked List is empty");
            } 
            else {  
                //Current will point to head  
                for(current = Head; current.next != null; current = current.next) {  
                    //Index will point to node next to current  
                    for(index = current.next; index != null; index = index.next) {  
                        //Bubble sort
                        if(current.data > index.data) {  
                            temp = current.data;  
                            current.data = index.data;  
                            index.data = temp;  
                        }  
                    }  
                }  
            }              
        }

        //Return the length of the list
        public int lenght() {
            int length = 1;

            Node current = Head;

            if (Head.next == null) {
                return 0;
            }

            while (current.next != null) {
                length += 1;
                current = current.next;
            }
            return length;
        }

        //return the value at given index
        public int getValue(int index) {
            Node current = Head;
            int i;
            for (i = 0; i < index; i++) {
                current = current.next;
            }
            return current.data;
        }

    }
    // DLL for numbers and their frequencies

    public static class FreqDLL {

        class Node {

            public int data;
            public int frequency;
            public Node previous;
            public Node next;

            public Node(int data, int frequency) {
                this.data = data;
                this.frequency = frequency;
            }
        }

        //Creation of Head and Tail Node
        Node Head = null;
        Node Tail = null;

        //Add Node to DLL
        public void addNodeDLL(int data, int frequency) {
            FreqDLL.Node newNode = new FreqDLL.Node(data, frequency);
            if (Head == null) {
                Head = Tail = newNode;
                Head.previous = null;
                Tail.next = null;
            } else {
                Tail.next = newNode;
                newNode.previous = Tail;
                Tail = newNode;
                Tail.next = null;
            }
        }

        public void printList() {
            if (Head == null) {
                System.out.println("Doubly Linked List is empty");
                return;
            }
            System.out.println("Nodes in Doubly Linked List: ");
            Node currentNode = Head;
            while (currentNode != null) {
                System.out.print(currentNode.data + " " + currentNode.frequency + "\n");
                currentNode = currentNode.next;
            }
            System.out.print('\n');
        }

        public int IsInlist(int value) {
            Node current = Head;

            while (current != null) {
                if (current.data == value) {
                    return 1;
                }
                current = current.next;
            }

            return 0;
        }

        public int getIndex(int value) {

            Node current = Head;
            int index = 0;

            while (current != null) {
                if (current.data == value) {
                    return index;
                }
                index += 1;
            }

            return 0;
        }

        public void IncrementFreqByOne(int value) {

            Node current = Head;

            while (current != null) {
                if (current.data == value) {
                    current.frequency += 1;
                    return;
                }
                current = current.next;
            }
        }

        public int lenght() {
            int length = 1;

            Node current = Head;

            if (Head.next == null) {
                return 0;
            }

            while (current.next != null) {
                length += 1;
                current = current.next;
            }
            return length;
        }

        public int getValue(int index) {
            Node current = Head;
            int i;
            for (i = 0; i < index; i++) {
                current = current.next;
            }
            return current.data;
        }

        public int getFreq(int index) {
            Node current = Head;
            int i;
            for (i = 0; i < index; i++) {
                current = current.next;
            }
            return current.frequency;
        }

    }

    public static void Counter(DataDLL dLinkedList, FreqDLL FrequencyList) {
        dLinkedList.sortList();
        int val;
        for (int i = 0; i < dLinkedList.lenght(); i++) {

            val = dLinkedList.getValue(i);
            if (FrequencyList.IsInlist(val) == 1) {
                FrequencyList.IncrementFreqByOne(dLinkedList.getValue(i));
            } else {
                FrequencyList.addNodeDLL(val, 1);
            }

        }
    }

    public static FreqDLL Frequency(File f, DataDLL dLinkedList) {
        //dLinkedList.printList();

        FreqDLL FrequencyList = new FreqDLL();
        Counter(dLinkedList, FrequencyList);

        //FrequencyList.printList();
        try {
            PrintWriter pw;
            pw = new PrintWriter("out.csv");
            pw.println("Data" + "," + "Frequency");

            for (int k = 0; k < FrequencyList.lenght(); k++) {

                pw.println(FrequencyList.getValue(k) + "," + FrequencyList.getFreq(k));
            }
            pw.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FrequencyOfNumberInArray.class.getName()).log(Level.SEVERE, null, ex);
        }
        return FrequencyList;
    }

    public static File InputFileDialog(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File f = fileChooser.showOpenDialog(stage);

        return f;
    }

    public static void main(String[] args) {

        launch(args);
    }

    public static class row {

        private final SimpleStringProperty Value;
        private final SimpleStringProperty Frequency;

        private row(String val, String freq) {
            this.Value = new SimpleStringProperty(val);
            this.Frequency = new SimpleStringProperty(freq);
        }

        public String getValue() {
            return Value.get();
        }

        public void setValue(String val) {
            Value.set(val);
        }

        public String getFrequency() {
            return Frequency.get();
        }

        public void setFrequency(String freq) {
            Frequency.set(freq);
        }
    }

}
