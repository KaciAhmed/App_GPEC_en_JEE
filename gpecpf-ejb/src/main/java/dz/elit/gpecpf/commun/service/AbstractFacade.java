package dz.elit.gpecpf.commun.service;

import dz.elit.gpecpf.commun.util.FieldValueMatchMode;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author leghettas.rabah
 * @param <T>
 */
public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) throws Exception {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) throws Exception {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) throws Exception {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAllOrderById() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root c = cq.from(entityClass);
        cq.select(c);
        cq.orderBy(cb.asc(c.get("id")));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAllOrderByAttribut(String attribut) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root c = cq.from(entityClass);
        cq.select(c);
        cq.orderBy(cb.asc(c.get(attribut)));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public List<T> lazyFilter(int first, int pageSize, String sortField, String sortOrder, List<FieldValueMatchMode> fieldValueMatchModes) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);

        Root<T> myObj = cq.from(entityClass);
        cq.where(this.getFilterCondition(cb, myObj, fieldValueMatchModes));
        if (sortField != null) {
            if (sortOrder.equals("ASCENDING")) {
                cq.orderBy(cb.asc(myObj.get(sortField)));
            } else {
                cq.orderBy(cb.desc(myObj.get(sortField)));
            }
        }
        return getEntityManager().createQuery(cq).setFirstResult(first).setMaxResults(pageSize).getResultList();
    }

    public int count(List<FieldValueMatchMode> fieldValueMatchModes) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> myObj = cq.from(entityClass);
        cq.where(this.getFilterCondition(cb, myObj, fieldValueMatchModes));
        cq.select(cb.count(myObj));
        return getEntityManager().createQuery(cq).getSingleResult().intValue();
    }

    private Predicate getFilterCondition(CriteriaBuilder cb, Root<T> myObj, List<FieldValueMatchMode> fieldValueMatchModes) {
        Predicate filterCondition = cb.conjunction();
        for (FieldValueMatchMode fieldValueMatchMode : fieldValueMatchModes) {
            String field = fieldValueMatchMode.getName();
            String value = fieldValueMatchMode.getValue();
            String matchMode = fieldValueMatchMode.getMatchMode();
            if (!field.equals("")) {
                javax.persistence.criteria.Path<String> path = myObj.get(field);
                switch (matchMode) {
                    case "STARTS_WITH":
                        filterCondition = cb.and(filterCondition, cb.like(cb.lower(path), value.toLowerCase() + "%"));
                        break;
                    case "ENDS_WITH":
                        filterCondition = cb.and(filterCondition, cb.like(cb.lower(path), "%" + value.toLowerCase()));
                        break;
                    case "CONTAINS":
                        filterCondition = cb.and(filterCondition, cb.like(cb.lower(path), "%" + value.toLowerCase() + "%"));
                        break;
                    case "EXACT":
                        filterCondition = cb.and(filterCondition, cb.equal(cb.lower(path), value.toLowerCase()));
                        break;
                    case "LESS_THAN":
                        filterCondition = cb.and(filterCondition, cb.lessThan(cb.lower(path), value));
                        break;
                    case "LESS_THAN_EQUALS":
                        filterCondition = cb.and(filterCondition, cb.lessThanOrEqualTo(cb.lower(path), value));
                        break;
                    case "GREATER_THAN":
                        filterCondition = cb.and(filterCondition, cb.greaterThan(cb.lower(path), value));
                        break;
                    case "GREATER_THAN_EQUALS":
                        filterCondition = cb.and(filterCondition, cb.greaterThanOrEqualTo(cb.lower(path), value));
                        break;
                    default:
                        filterCondition = cb.and(filterCondition, cb.like(cb.lower(path), "%" + value.toLowerCase() + "%"));
                        break;
                }
            }
        }
        return filterCondition;
    }
    
    public List<T> findByAttributs(String sortField, String sortOrder, List<FieldValueMatchMode> fieldValueMatchModes) {
         CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);

        Root<T> myObj = cq.from(entityClass);
        cq.where(this.getFilterCondition(cb, myObj, fieldValueMatchModes));
        if (sortField != null) {
            if (sortOrder.equals("ASCENDING")) {
                cq.orderBy(cb.asc(myObj.get(sortField)));
            } else {
                cq.orderBy(cb.desc(myObj.get(sortField)));
            }
        }
        return getEntityManager().createQuery(cq).getResultList();
    }
}
