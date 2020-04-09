/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dz.elit.gpecpf.gestion_des_competences.service;

import dz.elit.gpecpf.commun.service.AbstractFacade;
import dz.elit.gpecpf.commun.util.StaticUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import otherEntity.Typecompetence;


/**
 *
 * @author Dell
 */
@Stateless
public class TypeCompetenceFacade extends AbstractFacade<Typecompetence> {
    
    @PersistenceContext(unitName = StaticUtil.UNIT_NAME)
    private EntityManager em;
    
    public TypeCompetenceFacade(){
        super(Typecompetence.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    
}
