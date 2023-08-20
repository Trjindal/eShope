package com.eshope.Repository;

import com.eShope.common.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Integer> {

    @Query("SELECT q FROM Question q WHERE q.approved = true AND q.product.id = ?1")
    Page<Question> findByProduct(Integer productId, Pageable pageable);

    @Query("SELECT q FROM Question q WHERE q.approved = true AND q.product.alias = ?1")
    Page<Question> findQuestionByProductAlias(String alias, Pageable pageable);

    @Query("SELECT COUNT (q) FROM Question q WHERE q.approved = true AND q.answer IS NOT NULL and q.product.id =?1")
    int countAnsweredQuestions(Integer productId);

    @Query("SELECT COUNT (q) FROM Question q WHERE q.approved = true and q.product.id =?1")
    int countApprovedQuestions(Integer productId);



    @Query("SELECT q FROM Question q WHERE q.asker.id = ?1")
    Page<Question> findByCustomer(Integer customerId, Pageable pageable);

    @Query("SELECT q FROM Question q WHERE q.asker.id = ?1 AND ("
            + "q.questionContent LIKE %?2% OR "
            + "q.answer LIKE %?2% OR q.product.name LIKE %?2%)")
    Page<Question> findByCustomer(Integer customerId, String keyword, Pageable pageable);

    @Query("SELECT q FROM Question q WHERE q.asker.id = ?1 AND q.id = ?2")
    Question findByCustomerAndId(Integer customerId, Integer questionId);
}
