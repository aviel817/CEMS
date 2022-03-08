package client.gui;

import static common.ModelWrapper.Operation.EXTENSION_CONFIRM;
import static common.ModelWrapper.Operation.GET_EXTENSION_REQUESTS;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import client.Client;
import client.ClientUI;
import common.ModelWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ExamExtension;

/**
 * TimeExtentionController class handle confirmation of time extensions by the principal screen. 
 *
 */
public class TimeExtentionController implements Initializable {

	@FXML
	private TableView<ExamExtension> tvExtension;

	@FXML
	private TableColumn<ExamExtension, String> tcExamID;

	@FXML
	private TableColumn<ExamExtension, String> tcTeacherName;

	@FXML
	private TableColumn<ExamExtension, String> tcDuration;

	@FXML
	private TableColumn<ExamExtension, String> tcExtension;

	@FXML
	private TableColumn<ExamExtension, String> tcCause;

	@FXML
	private TableColumn<ExamExtension, JFXButton> tcConfirm;

	/**
	 * Setting the table and inserting data
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tcExamID.setCellValueFactory(new PropertyValueFactory<ExamExtension, String>("examID"));
		tcTeacherName.setCellValueFactory(new PropertyValueFactory<ExamExtension, String>("teacherName"));
		tcDuration.setCellValueFactory(new PropertyValueFactory<ExamExtension, String>("examDuration"));
		tcExtension.setCellValueFactory(new PropertyValueFactory<ExamExtension, String>("timeExtension"));
		tcCause.setCellValueFactory(new PropertyValueFactory<ExamExtension, String>("casue"));
		tcConfirm.setCellValueFactory(new PropertyValueFactory<ExamExtension, JFXButton>("confirmButton"));

		ModelWrapper<String> modelWrapper = new ModelWrapper<>(GET_EXTENSION_REQUESTS);
		ClientUI.getClientController().sendClientUIRequest(modelWrapper);

		List<ExamExtension> examExtensionList = Client.getExamExtensions();
		ObservableList<ExamExtension> examExtensions = FXCollections.observableArrayList();
		examExtensionList = addButtons(examExtensionList, examExtensions);
		examExtensions.addAll(examExtensionList);
		tvExtension.setItems(examExtensions);

	}

	/**
	 * adding buttons to the objects for confirmation of time extension
	 * @param examExtensionList list of exams with time extension requests
	 * @param examExtensions 
	 * @return
	 */
	private List<ExamExtension> addButtons(List<ExamExtension> examExtensionList,
			ObservableList<ExamExtension> examExtensions) {

		for (ExamExtension extension : examExtensionList) {
			JFXButton confirmButton = new JFXButton();
			confirmButton.setOnAction(new OnClickConfirm(extension, tvExtension));
			confirmButton.setPrefSize(90, 15);
			confirmButton
					.setStyle("-fx-background-color:#48a832;" + "-fx-background-radius:10;" + "-fx-text-fill:white;");
			confirmButton.setText("Confirm");
			extension.setConfirmButton(confirmButton);

		}
		return examExtensionList;
	}

	/**
	 * Handler for confirm button
	 *
	 */
	private class OnClickConfirm implements EventHandler<ActionEvent> {

		private ExamExtension extension;
		private TableView<ExamExtension> tableView;

		public OnClickConfirm(ExamExtension extension, TableView<ExamExtension> tableView) {
			this.extension = extension;
			this.tableView = tableView;
		}

		@Override
		public void handle(ActionEvent event) {
			tableView.getItems().remove(extension);
			extension.setConfirmButton(null);
			ModelWrapper<ExamExtension> modelWrapper = new ModelWrapper<>(extension, EXTENSION_CONFIRM);
			ClientUI.getClientController().sendClientUIRequest(modelWrapper);
		}

	}

}
