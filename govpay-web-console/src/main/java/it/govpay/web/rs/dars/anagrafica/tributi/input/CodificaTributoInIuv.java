package it.govpay.web.rs.dars.anagrafica.tributi.input;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.TipiTributoBD;
import it.govpay.model.TipoTributo;
import it.govpay.web.rs.dars.model.RawParamValue;
import it.govpay.web.rs.dars.model.input.dinamic.InputText;
import it.govpay.web.utils.Utils;

public class CodificaTributoInIuv extends InputText {

	private String idTipoTributoId= null;
	//	private String tributoId = null;
	private String nomeServizio = null;
	
	public CodificaTributoInIuv(String nomeServizio,String id, String label, int minLength, int maxLength, URI refreshUri,List<RawParamValue> values, Object... objects) {
		super(id, label, minLength, maxLength, refreshUri, values);
		this.nomeServizio = nomeServizio;
		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
		this.idTipoTributoId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".idTipoTributo.id");
		//		this.tributoId = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".id.id");
	}
	
	
	@Override
	protected boolean isHidden(List<RawParamValue> values, Object... objects) {
		return false;
	}

	@Override
	protected boolean isRequired(List<RawParamValue> values, Object... objects) {
		return false;
	}

	@Override
	protected boolean isEditable(List<RawParamValue> values, Object... objects) {
		String idTipoTributoValue = Utils.getValue(values, this.idTipoTributoId);
		if(StringUtils.isNotEmpty(idTipoTributoValue)){
			return true;
		}

		return false;
	}


	@Override
	protected String getDefaultValue(List<RawParamValue> values, Object... objects) {
		String idTipoTributoValue = Utils.getValue(values, this.idTipoTributoId);
		// Imposto la label di default
		Locale locale = objects[1] != null ? (Locale) objects[1] : null;
		this.label = Utils.getInstance(locale).getMessageFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.label");
		//		String idTributo = Utils.getValue(values, this.tributoId);

		if(StringUtils.isEmpty(idTipoTributoValue)){
			return null;
		}

		try {
			BasicBD bd = (BasicBD) objects[0];
			TipiTributoBD tipiTributoBD = new TipiTributoBD(bd);
			TipoTributo tipoTributo = tipiTributoBD.getTipoTributo(Long.parseLong(idTipoTributoValue));
			String codTributoIuvDefault = tipoTributo.getCodTributoIuvDefault();

			// prelevo il valore dal parent e lo inserisco nella label
			if(codTributoIuvDefault != null) {
				this.label = Utils.getInstance(locale).getMessageWithParamsFromResourceBundle(this.nomeServizio + ".codificaTributoInIuv.label.default.form",codTributoIuvDefault);
			}

		} catch (Exception e) {
			//throw new ServiceException(e);
		}

		return null;
	}

}
