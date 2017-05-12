/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.core.business;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.pagamento.FrBD;
import it.govpay.bd.pagamento.IncassiBD;
import it.govpay.bd.pagamento.PagamentiBD;
import it.govpay.core.business.model.RichiestaIncassoDTO;
import it.govpay.core.business.model.RichiestaIncassoDTOResponse;
import it.govpay.core.exceptions.IncassiException;
import it.govpay.core.exceptions.IncassiException.FaultType;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Rendicontazione.StatoRendicontazione;


public class Incassi extends BasicBD {

	private static Logger log = LogManager.getLogger();

	public Incassi(BasicBD basicBD) {
		super(basicBD);
	}

	public RichiestaIncassoDTOResponse richiestaIncasso(RichiestaIncassoDTO richiestaIncasso) throws NotAuthorizedException, InternalException, IncassiException {
			
		try {
			GpThreadLocal.get().log("incasso.richiesta");
			
			// TODO
			/* if(richiestaIncasso.getPrincipal)
			 *   throw new NotAuthorizedException
			 */
			
			RichiestaIncassoDTOResponse richiestaIncassoResponse = new RichiestaIncassoDTOResponse();
			
			// Controllo se il TRN dell'incasso e' gia registrato
			IncassiBD incassiBD = new IncassiBD(this);
			try {
				Incasso incasso = incassiBD.getIncasso(richiestaIncasso.getTrn());
				richiestaIncassoResponse.setPagamenti(incasso.getPagamenti(this));
				richiestaIncassoResponse.setCreato(false);
				return richiestaIncassoResponse;
			} catch(NotFoundException nfe) {
				// Incasso non registrato.
				richiestaIncassoResponse.setCreato(true);
			}
			
			
			// Validazione della causale
			String causale = richiestaIncasso.getCausale();
			String iuv = null;
			String idf = null;
			
			try {
				if(causale != null) {
					// Riversamento singolo
					if(causale.startsWith("/RFS/") || causale.startsWith("/RFB/")) {
						if(causale.indexOf("/", 6) != -1)
							iuv = causale.substring(5, causale.indexOf("/", 6));
						else
							iuv = causale.substring(5);
					}
					
					if(causale.startsWith("/PUR/LGPE-RIVERSAMENTO/URI/")) {
						idf = causale.substring(28);
					}
				}
			} catch (Throwable e) {
				log.error("Riscontrato errore durante il parsing della causale",e);
			} finally {
				if(iuv == null && idf==null) {
					GpThreadLocal.get().log("incasso.causaleNonValida", causale);
					throw new IncassiException(FaultType.CAUSALE_NON_VALIDA, "La causale dell'operazione di incasso non e' conforme alle specifiche AgID (SACIV 1.2.1): " + causale);
				}
			}

			// Sto selezionando i pagamenti per impostarli come Incassati.
			this.enableSelectForUpdate();
			
			// Riversamento singolo
			if(iuv != null) {
				PagamentiBD pagamentiBD = new PagamentiBD(this);
				try {
					it.govpay.bd.model.Pagamento pagamento = pagamentiBD.getPagamento(richiestaIncasso.getCodDominio(), iuv);
					richiestaIncassoResponse.getPagamenti().add(pagamento);
				} catch (NotFoundException nfe) {
					GpThreadLocal.get().log("incasso.iuvNonTrovato", iuv);
					throw new IncassiException(FaultType.PAGAMENTO_NON_TROVATO, "Lo IUV " + iuv + " estratto dalla causale di incasso non identifica alcun pagamento per il creditore " + richiestaIncasso.getCodDominio());
				} catch (MultipleResultException mre) {
					GpThreadLocal.get().log("incasso.iuvPagamentiMultipli", iuv, richiestaIncasso.getCodDominio());
					throw new IncassiException(FaultType.PAGAMENTO_NON_IDENTIFICATO, "Lo IUV " + iuv + " estratto dalla causale di incasso identifica piu' di un pagamento per il creditore " + richiestaIncasso.getCodDominio());
				}
			}
			
			// Riversamento cumulativo
			if(idf != null) {
				FrBD frBD = new FrBD(this);
				try {
					it.govpay.bd.model.Fr fr = frBD.getFr(idf);
					if(!fr.getStato().equals(StatoFr.ACCETTATA)) {
						GpThreadLocal.get().log("incasso.frAnomala", idf);
						throw new IncassiException(FaultType.FR_ANOMALA, "Il flusso di rendicontazione " + idf + " identificato dalla causale di incasso risulta avere delle anomalie");
					}
					
					for(Rendicontazione rendicontazione : fr.getRendicontazioni(this)) {
						if(!rendicontazione.getStato().equals(StatoRendicontazione.OK)) {
							GpThreadLocal.get().log("incasso.frAnomala", idf);
							throw new IncassiException(FaultType.FR_ANOMALA, "Il flusso di rendicontazione " + idf + " identificato dalla causale di incasso risulta avere delle anomalie");
						}
						
						it.govpay.bd.model.Pagamento pagamento = rendicontazione.getPagamento(this);
						richiestaIncassoResponse.getPagamenti().add(pagamento);
					}
				} catch (NotFoundException nfe) {
					GpThreadLocal.get().log("incasso.idfNonTrovato", idf);
					throw new IncassiException(FaultType.IDF_NON_TROVATO, "L'identificativo " + idf + " estratto dalla causale di incasso non identifica alcun flusso di rendicontazione");
				} 
			}
			
			// Verifica stato dei pagamenti da incassare e calcolo dell'importo pagato
			BigDecimal totalePagato = BigDecimal.ZERO;
			for(it.govpay.bd.model.Pagamento pagamento : richiestaIncassoResponse.getPagamenti()) {
				if(pagamento.getStato().equals(Stato.INCASSATO)) {
					GpThreadLocal.get().log("incasso.pagamentoGiaIncassato", pagamento.getCodDominio(), pagamento.getIuv(), pagamento.getIur());
					throw new IncassiException(FaultType.PAGAMENTO_GIA_INCASSATO, "Uno dei pagamenti incassati [Dominio:" + pagamento.getCodDominio() + " Iuv:" + pagamento.getIuv() + " Iur:" + pagamento.getIur() + "] risuta gia' incassato.");
				}
				totalePagato = totalePagato.add(pagamento.getImportoPagato());
			}
			
			// Verifica importo pagato con l'incassato
			if(totalePagato.compareTo(richiestaIncasso.getImporto()) != 0) {
				GpThreadLocal.get().log("incasso.importoErrato", totalePagato.doubleValue() + "", richiestaIncasso.getImporto().doubleValue() + "");
				throw new IncassiException(FaultType.IMPORTO_ERRATO, "L'importo incassato [" + richiestaIncasso.getImporto() + "] non corriponde con la somma dei pagamenti [" + totalePagato.doubleValue() + "]");
			}
			
			// Inserisco l'incasso e aggiorno lo stato dei pagamenti
			setAutoCommit(false);
			try {
				it.govpay.bd.model.Incasso incasso = new it.govpay.bd.model.Incasso();
				incasso.setCausale(richiestaIncasso.getCausale());
				incasso.setCodDominio(richiestaIncasso.getCodDominio());
				incasso.setDataAcquisizione(new Date());
				incasso.setDataValuta(richiestaIncasso.getData_valuta());
				incasso.setDispositivo(richiestaIncasso.getDispositivo());
				incasso.setImporto(richiestaIncasso.getImporto());
				incasso.setTrn(richiestaIncasso.getTrn());
				
				incassiBD.insertIncasso(incasso);
				
				PagamentiBD pagamentiBD = new PagamentiBD(this);
				for(it.govpay.bd.model.Pagamento pagamento : richiestaIncassoResponse.getPagamenti()) {
					pagamento.setStato(Stato.INCASSATO);
					pagamentiBD.updatePagamento(pagamento);
				}
				commit();
			} catch(Exception e) {
				rollback();
				throw new InternalException(e);
			} finally {
				setAutoCommit(true);
			}
			
			return richiestaIncassoResponse;
		} catch (ServiceException e) {
			throw new InternalException(e);
		}
	}
}