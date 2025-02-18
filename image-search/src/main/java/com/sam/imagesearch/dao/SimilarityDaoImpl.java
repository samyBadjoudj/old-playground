package com.sam.imagesearch.dao;

import com.sam.imagesearch.entity.Similarity;
import com.sam.imagesearch.entity.Image;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Created by Samy Badjoudj
 */
public class SimilarityDaoImpl implements SimilarityDao {

    private final SessionFactory sessionFactory;

    public SimilarityDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAllSimilarities(Collection<Similarity> similarities) {

        for (Similarity similarity : similarities) {
            sessionFactory.getCurrentSession().save(similarity);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteAllSimilarities(Long imageId) {
        List<Similarity> similarities = sessionFactory.getCurrentSession().createCriteria(Similarity.class).add(Restrictions.or(
                Restrictions.eq("image1.id", imageId),
                Restrictions.eq("image2.id", imageId))).list();
        for (Similarity similarity : similarities) {
            sessionFactory.getCurrentSession().delete(similarity);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Similarity> getImageNearestDistance(Image image) {


        return sessionFactory.getCurrentSession().createCriteria(Similarity.class).add(Restrictions.or(
                Restrictions.eq("image1.id", image.getId()),
                Restrictions.eq("image2.id", image.getId()))).
                addOrder(Order.desc("similarityValue")).list();
    }
}
