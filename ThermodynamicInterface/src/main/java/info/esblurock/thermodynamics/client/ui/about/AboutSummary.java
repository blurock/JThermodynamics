package info.esblurock.thermodynamics.client.ui.about;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTitle;
import info.esblurock.thermodynamics.client.view.AboutSummaryView;

public class AboutSummary extends Composite implements AboutSummaryView {

	Presenter listener;

	private static AboutSummaryUiBinder uiBinder = GWT.create(AboutSummaryUiBinder.class);

	interface AboutSummaryUiBinder extends UiBinder<Widget, AboutSummary> {
	}
	
	@UiField
	MaterialTitle title;
	@UiField
	MaterialLink jthergas;
	@UiField
	MaterialLink jthergasdescription;
	@UiField
	MaterialLink blurockconsultingab;
	@UiField
	MaterialLink blurockconsultingabdescription;
	@UiField
	MaterialLink blurock;
	@UiField
	MaterialLink blurockdescription;

	public AboutSummary() {
		initWidget(uiBinder.createAndBindUi(this));
		init();
	}

	private void init() {
		title.setTitle("Information about JTHERGAS");
		title.setDescription("The links below will open a new window");
		jthergas.setText("about JTHERGAS");
		jthergasdescription.setText("Structure and philosophy of JTHERGAS and tutorials about the use of JTHERGAS");
		blurockconsultingab.setText("Blurock Consulting AB");
		blurockconsultingabdescription.setText("Information about Blurock Consulting AB, data manaagement and Edward S. Blurock");
		blurock.setText("Edward S. Blurock");
		blurockdescription.setText("Information about the developer of CHEMCONNECT Edward S. Blurock");
	}
	
	@Override
	public void setName(String helloName) {
	}
	
	@UiHandler("jthergas")
	public void chemconnectClicked(ClickEvent event) {
		Window.open("https://sites.google.com/view/jthergas/home", "_blank", "");
	}

	@UiHandler("blurockconsultingab")
	public void blurockconsultingabClicked(ClickEvent event) {
		Window.open("https://sites.google.com/view/blurock-consulting-ab", "_blank", "");
	}
	@UiHandler("jthergasdescription")
	public void chemconnectDescriptionClicked(ClickEvent event) {
		Window.open("https://sites.google.com/view/jthergas/home", "_blank", "");
	}

	@UiHandler("blurockconsultingabdescription")
	public void blurockconsultingabDescriptionClicked(ClickEvent event) {
		Window.open("https://sites.google.com/view/blurock-consulting-ab", "_blank", "");
	}
	@UiHandler("blurock")
	public void blurockClicked(ClickEvent event) {
		Window.open("https://sites.google.com/view/blurock-consulting-ab/edward-s-blurock", "_blank", "");
	}
	@UiHandler("blurockdescription")
	public void blurockDescriptionClicked(ClickEvent event) {
		Window.open("https://sites.google.com/view/blurock-consulting-ab/edward-s-blurock", "_blank", "");
	}
	
	@Override
	public void setPresenter(Presenter listener) {
		this.listener = listener;	
	}


}
