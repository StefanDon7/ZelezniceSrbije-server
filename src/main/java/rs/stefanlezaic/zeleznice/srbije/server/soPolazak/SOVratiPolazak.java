/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.stefanlezaic.zeleznice.srbije.server.soPolazak;

import rs.stefanlezaic.zeleznice.srbije.lib.domen.GeneralEntity;
import rs.stefanlezaic.zeleznice.srbije.lib.domen.Linija;
import rs.stefanlezaic.zeleznice.srbije.lib.domen.Polazak;
import rs.stefanlezaic.zeleznice.srbije.lib.domen.Voz;
import rs.stefanlezaic.zeleznice.srbije.lib.exception.EntityNotFoundException;
import rs.stefanlezaic.zeleznice.srbije.lib.exception.InvalidProductException;
import java.sql.SQLException;
import rs.stefanlezaic.zeleznice.srbije.server.so.AbstractGenericOperation;
import rs.stefanlezaic.zeleznice.srbije.server.soLinija.SOVratiLiniju;

/**
 *
 * @author sleza
 */
public class SOVratiPolazak extends AbstractGenericOperation {

    private GeneralEntity polazak;

    @Override
    protected void validate(Object entity) throws Exception {
        if (!(entity instanceof Polazak)) {
            throw new Exception("Pogresni paremetri");
        }
        Polazak p = (Polazak) entity;
        if (p.getPolazakID() <= -1) {
            throw new InvalidProductException("Pogresni parametri!");
        }
    }

    @Override
    protected void execute(Object entity) throws EntityNotFoundException, SQLException, Exception {
        try {
            polazak = databaseBroker.findRecord((Polazak) entity);
            Polazak p = (Polazak) polazak;
            AbstractGenericOperation op7 = new SOVratiLiniju();
            op7.templateExecute(new Linija(p.getLinija().getLinijaID()));
            Linija l = (Linija) ((SOVratiLiniju) op7).getLinija();
            p.setLinija(l);
            p.setVoz((Voz) databaseBroker.findRecord(new Voz(p.getVoz().getVozID())));
        } catch (SQLException ex) {
            throw new SQLException("Greška na strani servera");
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException("Pogrešno pogresno");
        }
    }

    public GeneralEntity getPolazak() {
        return polazak;
    }

}
