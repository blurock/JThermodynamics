package info.esblurock.thermodynamics.client.ui.benson;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import info.esblurock.thermodynamics.base.benson.BensonThermodynamicBaseElements;
import info.esblurock.thermodynamics.base.benson.SetOfBensonThermodynamicBaseElements;

public class SetOfBensonThermodynamicElementsCollapsible extends Composite {

	private static SetOfBensonThermodynamicElementsCollapsibleUiBinder uiBinder = GWT
			.create(SetOfBensonThermodynamicElementsCollapsibleUiBinder.class);

	interface SetOfBensonThermodynamicElementsCollapsibleUiBinder
			extends UiBinder<Widget, SetOfBensonThermodynamicElementsCollapsible> {
	}

	public SetOfBensonThermodynamicElementsCollapsible() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	MaterialLink title;
	@UiField
	MaterialLink seeinfo;
	@UiField
	MaterialPanel infopanel;
	@UiField
	MaterialCollapsible topbodypanel;

	public SetOfBensonThermodynamicElementsCollapsible(String name, SetOfBensonThermodynamicBaseElements elements) {
		initWidget(uiBinder.createAndBindUi(this));
		title.setText("Thermodynamic Elements for " + name);
		int count = 0;
		for(BensonThermodynamicBaseElements element: elements) {
			String activator = "activates-" + count++;
			BensonThermodynamicBaseElementsCollapsible benson = new BensonThermodynamicBaseElementsCollapsible(activator,element);
			topbodypanel.add(benson);
		}
	}
}
