package quiz.ejb;

import quiz.entity.Quiz;
import quiz.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
public class QuizEjb {

    @PersistenceContext
    private EntityManager em;


    public long createQuiz(long subCategoryId, String question, String firstAnswer, String secondAnswer, String thirdAnswer, String fourthAnswer, int indexOfCorrectAnswer){

        SubCategory subCategory = em.find(SubCategory.class, subCategoryId);
        if(subCategory == null){
            throw new IllegalArgumentException("SubCategory " + subCategoryId + " does not exist");
        }

        Quiz quiz = new Quiz();
        quiz.setSubCategory(subCategory);
        quiz.setQuestion(question);
        quiz.setAnswer1(firstAnswer);
        quiz.setAnswer2(secondAnswer);
        quiz.setAnswer3(thirdAnswer);
        quiz.setAnswer4(fourthAnswer);
        quiz.setIndexOfCorrectAnswer(indexOfCorrectAnswer);

        em.persist(quiz);

        return quiz.getId();
    }

    public List<Quiz> getAllQuizzes(){
        TypedQuery<Quiz> query = em.createQuery("select q from Quiz q", Quiz.class);
        return query.getResultList();
    }

    public Quiz getQuiz(long id){
        Quiz quiz = em.find(Quiz.class, id);

        return quiz;
    }

    public List<Quiz> getRandomQuizzes(int n, long categoryId){

    TypedQuery<Long> sizeQuery = em.createQuery(
            "select count(q) from Quiz q where q.subCategory.parent.id=?1", Long.class);
    sizeQuery.setParameter(1, categoryId);
    long size = sizeQuery.getSingleResult();

    if(n > size){
        throw new IllegalArgumentException("Cannot choose " + n + " unique quizzes out of the " + size + " existing");
    }

    Random random = new Random();
    List<Quiz> quizzes = new ArrayList<>();
    Set<Integer> chosen = new HashSet<>();

    while (chosen.size() < n){

        int k = random.nextInt((int)size);
        if (chosen.contains(k)){
            continue;
        }
        chosen.add(k);

        TypedQuery<Quiz> query = em.createQuery(
                "select q from Quiz q where q.subCategory.parent.id=?1", Quiz.class);
        query.setParameter(1, categoryId);
        query.setMaxResults(1);
        query.setFirstResult(k);

        quizzes.add(query.getSingleResult());
    }

    return quizzes;

    }
}
