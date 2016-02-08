/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.orm.dao.jdbc.converter;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.IModel;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.expression.impl.sql.AbstractSQLFieldConverter;
import org.openspcoop2.utils.TipiDatabase;

import it.govpay.orm.SingoloVersamento;


/**     
 * SingoloVersamentoFieldConverter
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingoloVersamentoFieldConverter extends AbstractSQLFieldConverter {

	private TipiDatabase databaseType;
	
	public SingoloVersamentoFieldConverter(String databaseType){
		this.databaseType = TipiDatabase.toEnumConstant(databaseType);
	}
	public SingoloVersamentoFieldConverter(TipiDatabase databaseType){
		this.databaseType = databaseType;
	}


	@Override
	public IModel<?> getRootModel() throws ExpressionException {
		return SingoloVersamento.model();
	}
	
	@Override
	public TipiDatabase getDatabaseType() throws ExpressionException {
		return this.databaseType;
	}
	


	@Override
	public String toColumn(IField field,boolean returnAlias,boolean appendTablePrefix) throws ExpressionException {
		
		// In the case of columns with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the column containing the alias
		
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_versamento_ente";
			}else{
				return "cod_versamento_ente";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_DOMINIO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_dominio";
			}else{
				return "cod_dominio";
			}
		}
		if(field.equals(SingoloVersamento.model().INDICE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".indice";
			}else{
				return "indice";
			}
		}
		if(field.equals(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_singolo_versamento_ente";
			}else{
				return "cod_singolo_versamento_ente";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_TRIBUTO.ID_ENTE.COD_ENTE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_ente";
			}else{
				return "cod_ente";
			}
		}
		if(field.equals(SingoloVersamento.model().ID_TRIBUTO.COD_TRIBUTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".cod_tributo";
			}else{
				return "cod_tributo";
			}
		}
		if(field.equals(SingoloVersamento.model().ANNO_RIFERIMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".anno_riferimento";
			}else{
				return "anno_riferimento";
			}
		}
		if(field.equals(SingoloVersamento.model().IBAN_ACCREDITO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iban_accredito";
			}else{
				return "iban_accredito";
			}
		}
		if(field.equals(SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_singolo_versamento";
			}else{
				return "importo_singolo_versamento";
			}
		}
		if(field.equals(SingoloVersamento.model().IMPORTO_COMMISSIONI_PA)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".importo_commissioni_pa";
			}else{
				return "importo_commissioni_pa";
			}
		}
		if(field.equals(SingoloVersamento.model().SINGOLO_IMPORTO_PAGATO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".singolo_importo_pagato";
			}else{
				return "singolo_importo_pagato";
			}
		}
		if(field.equals(SingoloVersamento.model().CAUSALE_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".causale_versamento";
			}else{
				return "causale_versamento";
			}
		}
		if(field.equals(SingoloVersamento.model().DATI_SPECIFICI_RISCOSSIONE)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".dati_specifici_riscossione";
			}else{
				return "dati_specifici_riscossione";
			}
		}
		if(field.equals(SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".stato_singolo_versamento";
			}else{
				return "stato_singolo_versamento";
			}
		}
		if(field.equals(SingoloVersamento.model().ESITO_SINGOLO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".esito_singolo_pagamento";
			}else{
				return "esito_singolo_pagamento";
			}
		}
		if(field.equals(SingoloVersamento.model().DATA_ESITO_SINGOLO_PAGAMENTO)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".data_esito_singolo_pagamento";
			}else{
				return "data_esito_singolo_pagamento";
			}
		}
		if(field.equals(SingoloVersamento.model().IUR)){
			if(appendTablePrefix){
				return this.toAliasTable(field)+".iur";
			}else{
				return "iur";
			}
		}


		return super.toColumn(field,returnAlias,appendTablePrefix);
		
	}
	
	@Override
	public String toTable(IField field,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_VERSAMENTO_ENTE)){
			return this.toTable(SingoloVersamento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_VERSAMENTO.COD_DOMINIO)){
			return this.toTable(SingoloVersamento.model().ID_VERSAMENTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().INDICE)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().COD_SINGOLO_VERSAMENTO_ENTE)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_TRIBUTO.ID_ENTE.COD_ENTE)){
			return this.toTable(SingoloVersamento.model().ID_TRIBUTO.ID_ENTE, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ID_TRIBUTO.COD_TRIBUTO)){
			return this.toTable(SingoloVersamento.model().ID_TRIBUTO, returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ANNO_RIFERIMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().IBAN_ACCREDITO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().IMPORTO_SINGOLO_VERSAMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().IMPORTO_COMMISSIONI_PA)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().SINGOLO_IMPORTO_PAGATO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().CAUSALE_VERSAMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().DATI_SPECIFICI_RISCOSSIONE)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().STATO_SINGOLO_VERSAMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().ESITO_SINGOLO_PAGAMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().DATA_ESITO_SINGOLO_PAGAMENTO)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}
		if(field.equals(SingoloVersamento.model().IUR)){
			return this.toTable(SingoloVersamento.model(), returnAlias);
		}


		return super.toTable(field,returnAlias);
		
	}

	@Override
	public String toTable(IModel<?> model,boolean returnAlias) throws ExpressionException {
		
		// In the case of table with alias, using parameter returnAlias​​, 
		// it is possible to drive the choice whether to return only the alias or 
		// the full definition of the table containing the alias
		
		if(model.equals(SingoloVersamento.model())){
			return "singoli_versamenti";
		}
		if(model.equals(SingoloVersamento.model().ID_VERSAMENTO)){
			return "versamenti";
		}
		if(model.equals(SingoloVersamento.model().ID_TRIBUTO)){
			return "tributi";
		}
		if(model.equals(SingoloVersamento.model().ID_TRIBUTO.ID_ENTE)){
			return "id_ente";
		}


		return super.toTable(model,returnAlias);
		
	}

}
