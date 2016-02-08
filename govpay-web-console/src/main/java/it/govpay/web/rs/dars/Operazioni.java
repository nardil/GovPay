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
package it.govpay.web.rs.dars;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.utils.resources.GestoreRisorseJMX;

import it.govpay.bd.BasicBD;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.web.rs.BaseRsService;
import it.govpay.web.rs.dars.model.DarsResponse;
import it.govpay.web.rs.dars.model.DarsResponse.EsitoOperazione;

@Path("/dars/operazioni")
public class Operazioni extends BaseRsService {
	
	private static GestoreRisorseJMX gestoreJMX;
	private final static String ACQUISIZIONE_RENDICONTAZIONI = "acquisizioneRendicontazioni";
	private final static String AGGIORNAMENTO_REGISTRO_PSP = "aggiornamentoRegistroPsp";
	private final static String NOTIFICHE_MAIL = "notificheMail";
	private final static String RECUPERO_RPT_PENDENTI = "recuperoRptPendenti";
	private final static String SPEDIZIONE_ESITI = "spedizioneEsiti";
	private final static String RESET_CACHE_ANAGRAFICA = "resetCacheAnagrafica";
	
	public Operazioni() {
		try {
			gestoreJMX = new GestoreRisorseJMX(org.apache.log4j.Logger.getLogger(Operazioni.class));
		} catch (Exception e) {
			log.error("Errore nella inizializzazione del gestore JMX", e);
		}
	}

	Logger log = LogManager.getLogger();

	@GET
	@Path("/aggiornaPsp")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse aggiornamentoRegistroPsp(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {

		initLogger("DARSAggiornamentoRegistroPsp");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"]");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			gestoreJMX.invoke("it.govpay","type","operazioni", AGGIORNAMENTO_REGISTRO_PSP, null, null);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'aggiornamento del Registro Psp:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'aggiornamento del Registro Psp: " +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd!=null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@GET
	@Path("/recuperoRptPendenti")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse recuperoRptPendenti(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {
		initLogger("DARSRecuperoRptPendenti");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"]");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			gestoreJMX.invoke("it.govpay","type","operazioni", RECUPERO_RPT_PENDENTI, null, null);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'acquisizione delle Rpt pendenti:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'acquisizione delle Rpt pendenti:" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd!=null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@GET
	@Path("/acquisizioneRendicontazioni")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse acquisizioneRendicontazioni(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {
		initLogger("DARSAcquisizioneRendicontazioni");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"]");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			gestoreJMX.invoke("it.govpay","type","operazioni", ACQUISIZIONE_RENDICONTAZIONI, null, null);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante l'acquisizione delle Rendicontazioni:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante l'acquisizione delle Rendicontazioni:" +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd!=null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}

	@GET
	@Path("/resetCache")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse resetCache(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {
		initLogger("DARSResetCache");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"]");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			gestoreJMX.invoke("it.govpay","type","operazioni", RESET_CACHE_ANAGRAFICA, null, null);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante il reset della cache:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante il reset della cache: " +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd!=null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
	
	@GET
	@Path("/notificheMail")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse notificheMail(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {
		initLogger("DARSNotificheMail");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"]");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			gestoreJMX.invoke("it.govpay","type","operazioni", NOTIFICHE_MAIL, null, null);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la spedizione delle mail:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la spedizione delle mail: " +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd!=null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
	
	@GET
	@Path("/spedizioneEsiti")
	@Produces({MediaType.APPLICATION_JSON})
	public DarsResponse spedizioneEsiti(@QueryParam(value = "operatore") String principalOperatore) throws GovPayException {
		initLogger("DARSSpedizioneEsiti");
		log.info("Ricevuta richiesta: operatore["+principalOperatore+"]");

		DarsResponse darsResponse = new DarsResponse();
		darsResponse.setCodOperazione(this.codOperazione);
		BasicBD bd = null;
		try {
			try {
				bd = BasicBD.newInstance();
			} catch (Exception e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
			this.checkOperatoreAdmin(bd);
			gestoreJMX.invoke("it.govpay","type","operazioni", SPEDIZIONE_ESITI, null, null);
			darsResponse.setEsitoOperazione(EsitoOperazione.ESEGUITA);
		}catch(WebApplicationException e){
			log.error("Riscontrato errore di autorizzazione durante la spedizione degli esiti:" +e.getMessage() , e);
			throw e;
		} catch (Exception e) {
			log.error("Riscontrato errore durante la spedizione degli esiti: " +e.getMessage() , e);
			darsResponse.setEsitoOperazione(EsitoOperazione.ERRORE);
			darsResponse.setDettaglioEsito(GovPayExceptionEnum.ERRORE_INTERNO.toString() + ": " + e.getMessage());
		} finally {
			response.setHeader("Access-Control-Allow-Origin", "*");
			if(bd!=null) bd.closeConnection();
		}
		log.info("Richiesta evasa con successo");
		return darsResponse;
	}
}