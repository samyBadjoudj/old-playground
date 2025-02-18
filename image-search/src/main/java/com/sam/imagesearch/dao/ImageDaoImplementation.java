package com.sam.imagesearch.dao;

import com.sam.imagesearch.entity.Image;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.ejb.criteria.expression.EntityTypeExpression;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samy Badjoudj
 */
public class ImageDaoImplementation   implements ImageDao {

    private final SessionFactory sessionFactory;
    private static final Logger LOGGER = Logger.getLogger(ImageDaoImplementation.class);

    public ImageDaoImplementation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public Image getImageBySignal(String signal) {

        final List signals = getCriteria().add(Restrictions.eq("signal", signal)).list();

        if(signals.size() > 0){
            return (Image) signals.get(0);
          }
        return  new Image();
    }

    private Criteria getCriteria() {
        return sessionFactory.getCurrentSession().createCriteria(Image.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Long saveImage(Image image) {
        try {
            return (Long) sessionFactory.getCurrentSession().save(image);
        }catch (DataAccessException e){
            LOGGER.error("Can't save image entitie {}",e);
          return -1L;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteImage(Long imageId) {
        sessionFactory.getCurrentSession().delete(getImageById(imageId));
    }

    @Override
    @Transactional(readOnly =true )
    public List<Image> getAllImagesExceptOne(Image image) {
        return getCriteria().add(Restrictions.not(Restrictions.eq("id",image.getId()))).list();
    }

    @Override
    @Transactional(readOnly =true )
    public Image getImageById(long imageId) {

        return (Image) sessionFactory.getCurrentSession().byId(Image.class).load(imageId);
    }
}
