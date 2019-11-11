package info.esblurock.thermodynamics.client.ui.benson;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialLink;

public class HeatCapacityPairColumns extends Composite {

	private static HeatCapacityPairColumnsUiBinder uiBinder = GWT.create(HeatCapacityPairColumnsUiBinder.class);

	interface HeatCapacityPairColumnsUiBinder extends UiBinder<Widget, HeatCapacityPairColumns> {
	}

	public HeatCapacityPairColumns() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	MaterialLink value;

	public HeatCapacityPairColumns(String valueS) {
		initWidget(uiBinder.createAndBindUi(this));
		value.setText(valueS);
	}
}
