/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.backing;

import hu.unideb.studentSupportInterface.dao.AssessmentDao;
import hu.unideb.studentSupportInterface.dao.CommentDao;
import hu.unideb.studentSupportInterface.dao.EvaluationDao;
import hu.unideb.studentSupportInterface.dao.SolutionDao;
import hu.unideb.studentSupportInterface.dao.UserDao;
import hu.unideb.studentSupportInterface.model.Assessment;
import hu.unideb.studentSupportInterface.model.Comment;
import hu.unideb.studentSupportInterface.model.Evaluation;
import hu.unideb.studentSupportInterface.model.Role;
import hu.unideb.studentSupportInterface.model.Solution;
import hu.unideb.studentSupportInterface.model.User;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Adam
 */
public class ViewSolution implements Serializable {

    private Solution solution;
    private SolutionDao solutionDao;
    private StreamedContent file;
    private Assessment newAssessment;
    private UserDao userDao;
    private CommentDao commentDao;
    private AssessmentDao assessmentDao;
    private Comment newComment;
    private User user;
    private List<Assessment> assessments;
    private List<Comment> comments;
    private HashMap<String, Comment> commentMap;
    private HashMap<String, Assessment> assessmentMap;
    private HashMap<String, Integer> commentAssessmentMap2;
    private int count;
    private List<String> keys;
    private HashMap<String, String> timeMap;
    private Comment selectedComment;
    private HashMap<Comment, List<Comment>> replyMap;
    private HashMap<Comment, String> repliesTimeMap;
    private EvaluationDao evaluationDao;
    private Assessment selectedAssessment;
    private HashMap<Assessment, Integer> evaluationMap;
    private HashMap<Assessment, Integer> userEvaluationMap;

    @PostConstruct
    public void initBean() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        user = (User) userDao.loadUserByUsername(auth.getName());

        if (flash.get("solution_id") != null) {
            solution = solutionDao.getSolutionById((Integer) flash.get("solution_id"));
        }
        if (solution.getFile() != null) {
            file = new DefaultStreamedContent(solution.getFile(), "application/zip", solution.getId() + ".zip");

        }

        newAssessment = new Assessment();
        newAssessment.setComment(new Comment());

        newComment = new Comment();
        newComment.setSolution(solution);
        newComment.setUser(user);

        comments = commentDao.getCommentsBySolution(solution);
        assessments = assessmentDao.getAssessmentsBySolution(solution);

        for (Assessment a : assessments) {
            if (a.getComment() != null) {
                comments.remove(a.getComment());
            }
        }

        commentMap = new HashMap<String, Comment>();
        assessmentMap = new HashMap<String, Assessment>();
        commentAssessmentMap2 = new HashMap<String, Integer>();
        timeMap = new HashMap<String, String>();
        keys = new ArrayList<String>();
        replyMap = new HashMap<Comment, List<Comment>>();
        repliesTimeMap = new HashMap<Comment, String>();
        evaluationMap = new HashMap<Assessment, Integer>();
        int i = 0;
        int j = 0;
        for (int k = 0; k < comments.size() + assessments.size(); k++) {
            keys.add(Integer.toString(k));
            if (i >= comments.size()) {
                commentAssessmentMap2.put(Integer.toString(k), 2);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                timeMap.put(Integer.toString(k), format.format(assessments.get(j).getTime().getTime()));
                if (assessments.get(j).getComment() != null) {
                    replyMap.put(assessments.get(j).getComment(), new ArrayList<Comment>());
                }
                evaluationMap.put(assessments.get(j), evaluationDao.getEvaluationDifferenceForAssessment(assessments.get(j)));
                assessmentMap.put(Integer.toString(k), assessments.get(j++));

            } else if (j >= assessments.size()) {
                commentAssessmentMap2.put(Integer.toString(k), 1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                timeMap.put(Integer.toString(k), format.format(comments.get(i).getTime().getTime()));
                replyMap.put(comments.get(i), new ArrayList<Comment>());
                commentMap.put(Integer.toString(k), comments.get(i++));
            } else if (comments.get(i).getTime().before(assessments.get(j).getTime())) {
                commentAssessmentMap2.put(Integer.toString(k), 1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                timeMap.put(Integer.toString(k), format.format(comments.get(i).getTime().getTime()));
                replyMap.put(comments.get(i), new ArrayList<Comment>());
                commentMap.put(Integer.toString(k), comments.get(i++));
            } else {
                commentAssessmentMap2.put(Integer.toString(k), 2);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                timeMap.put(Integer.toString(k), format.format(assessments.get(j).getTime().getTime()));
                if (assessments.get(j).getComment() != null) {
                    replyMap.put(assessments.get(j).getComment(), new ArrayList<Comment>());
                }
                evaluationMap.put(assessments.get(j), evaluationDao.getEvaluationDifferenceForAssessment(assessments.get(j)));
                assessmentMap.put(Integer.toString(k), assessments.get(j++));
            }

        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String temp = it.next();
            if (commentMap.get(temp) != null && commentMap.get(temp).getReplyTo() != null) {
                replyMap.get(commentMap.get(temp).getReplyTo()).add(commentMap.get(temp));
                repliesTimeMap.put(commentMap.get(temp), format.format(commentMap.get(temp).getTime().getTime()));
                commentMap.remove(temp);
                it.remove();
            }
        }

        count = commentAssessmentMap2.size();

        userEvaluationMap = new HashMap<Assessment, Integer>();
        for (Assessment a : assessments) {
            Evaluation temp = evaluationDao.getEvaluationByAssessmentAndEvaluator(a, user);
            if (temp == null) {
                userEvaluationMap.put(a, 0);
            } else if (temp.isCorrect()) {
                userEvaluationMap.put(a, 1);
            } else {
                userEvaluationMap.put(a, -1);
            }
        }

    }

    public void addAssessment() {

        newAssessment.setAssessor(user);
        newAssessment.setSolution(solution);
        Calendar cal = Calendar.getInstance();
        newAssessment.setTime(cal);
        if (newAssessment.getComment().getText() != null && !newAssessment.getComment().getText().isEmpty()) {
            newAssessment.getComment().setSolution(solution);
            newAssessment.getComment().setUser(user);
        }
        newAssessment = assessmentDao.createAssessment(newAssessment);
        /*
         commentAssessmentMap2.put(Integer.toString(count), 2);
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
         timeMap.put(Integer.toString(count), format.format(newAssessment.getTime().getTime()));
         if (newAssessment.getComment() != null && !newAssessment.getComment().getText().isEmpty()) {
         replyMap.put(newAssessment.getComment(), new ArrayList<Comment>());
         }
         evaluationMap.put(newAssessment, 0);
         assessmentMap.put(Integer.toString(count), newAssessment);
         keys.add(Integer.toString(count));
         count++;
        
         newAssessment = new Assessment();
         newAssessment.setComment(new Comment());
         */
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sikeres értékelés.", "Sikeres értékelés."));
        initBean();

    }

    public void addComment() {
        newComment = commentDao.createComment(newComment);
        newComment = new Comment();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sikeres hozzászólás.", "Sikeres hozzászólás."));

        initBean();
    }

    public void addReplyComment() {
        newComment.setReplyTo(selectedComment);
        newComment = commentDao.createComment(newComment);
        newComment = new Comment();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "A válasz sikeresen elküldve.", "A válasz sikeresen elküldve."));

        initBean();
    }

    public void addPositiveEvaluation() {
        if (userEvaluationMap.get(selectedAssessment) == 0) {
            Evaluation eval = new Evaluation();
            eval.setCorrect(true);
            eval.setEvaluator(user);
            eval.setAssessment(selectedAssessment);
            eval = evaluationDao.createEvaluation(eval);
            int temp = evaluationMap.get(selectedAssessment);
            evaluationMap.remove(selectedAssessment);
            evaluationMap.put(selectedAssessment, temp + 1);
        } else if (userEvaluationMap.get(selectedAssessment) == -1) {
            Evaluation eval = evaluationDao.getEvaluationByAssessmentAndEvaluator(selectedAssessment, user);
            eval.setCorrect(true);
            evaluationDao.updateEvaluation(eval);
            int temp = evaluationMap.get(selectedAssessment);
            evaluationMap.remove(selectedAssessment);
            evaluationMap.put(selectedAssessment, temp + 2);
        }
        userEvaluationMap.remove(selectedAssessment);
        userEvaluationMap.put(selectedAssessment, 1);
        //initBean();
    }

    public void addNegativeEvaluation() {
        if (userEvaluationMap.get(selectedAssessment) == 0) {
            Evaluation eval = new Evaluation();
            eval.setCorrect(false);
            eval.setEvaluator(user);
            eval.setAssessment(selectedAssessment);
            eval = evaluationDao.createEvaluation(eval);
            int temp = evaluationMap.get(selectedAssessment);
            evaluationMap.remove(selectedAssessment);
            evaluationMap.put(selectedAssessment, temp - 1);

        } else if (userEvaluationMap.get(selectedAssessment) == 1) {
            Evaluation eval = evaluationDao.getEvaluationByAssessmentAndEvaluator(selectedAssessment, user);
            eval.setCorrect(false);
            evaluationDao.updateEvaluation(eval);
            int temp = evaluationMap.get(selectedAssessment);
            evaluationMap.remove(selectedAssessment);
            evaluationMap.put(selectedAssessment, temp - 2);
        }
        userEvaluationMap.remove(selectedAssessment);
        userEvaluationMap.put(selectedAssessment, -1);
        //initBean();
    }

    public void deleteComment() {
        commentDao.deleteComment(selectedComment);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sikeres törlés.", "Sikeres törlés."));

        initBean();
    }

    public void deleteAssessment() {
        assessmentDao.deleteAssessment(selectedAssessment);
        if (selectedAssessment.getComment() != null) {
            commentDao.deleteComment(selectedAssessment.getComment());
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sikeres törlés.", "Sikeres törlés."));

        initBean();
    }

    public boolean canAddAssessment() {
        for (Assessment a : assessments) {
            if (a.getAssessor().equals(user)) {
                return false;
            }

        }

        if (user.equals(solution.getUploader())) {
            return false;
        }
        return true;
    }

    public String entryWriter(Assessment assessment, Comment comment) {
        if (assessment != null) {
            if (assessment.getAssessor().getId() == user.getId()) {
                return "Én";
            }

            if (assessment.getAssessor().getId() == solution.getUploader().getId()) {
                return "Feltöltõ";
            }

            return "Felhasználó#" + assessment.getAssessor().getId();
        }

        if (comment != null) {
            if (comment.getUser().getId() == user.getId()) {
                return "Én";
            }

            if (comment.getUser().getId() == solution.getUploader().getId()) {
                return "Feltöltõ";
            }

            return "Felhasználó#" + comment.getUser().getId();
        }

        return null;
    }

    public String solutionWriter() {
        if (solution.getUploader().getId() == user.getId()) {
            return "Én";
        }
        if (solution.getUploader().isIsPublic() && user.getRoles().contains(Role.TUTOR)) {
            return solution.getUploader().getNeptunCode() == null ? solution.getUploader().getLastName() + " " + solution.getUploader().getFirstName()
                    : solution.getUploader().getLastName() + " " + solution.getUploader().getFirstName() + " (" + solution.getUploader().getNeptunCode() + ")";
        }

        return "Felhasználó#" + solution.getUploader().getId();

    }

    public boolean myComment(Comment comment) {
        return user.getId() == comment.getUser().getId();
    }

    public boolean myAssessment(Assessment assessment) {
        return user.getId() == assessment.getAssessor().getId();
    }

    public boolean mySolution() {
        return user.getId() == solution.getUploader().getId();
    }

    public Solution getSolution() {
        return solution;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    public SolutionDao getSolutionDao() {
        return solutionDao;
    }

    public void setSolutionDao(SolutionDao solutionDao) {
        this.solutionDao = solutionDao;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public Assessment getNewAssessment() {
        return newAssessment;
    }

    public void setNewAssessment(Assessment newAssessment) {
        this.newAssessment = newAssessment;
    }

    public AssessmentDao getAssessmentDao() {
        return assessmentDao;
    }

    public void setAssessmentDao(AssessmentDao assessmentDao) {
        this.assessmentDao = assessmentDao;
    }

    public CommentDao getCommentDao() {
        return commentDao;
    }

    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public Comment getNewComment() {
        return newComment;
    }

    public void setNewComment(Comment newComment) {
        this.newComment = newComment;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public HashMap<String, Comment> getCommentMap() {
        return commentMap;
    }

    public void setCommentMap(HashMap<String, Comment> commentMap) {
        this.commentMap = commentMap;
    }

    public HashMap<String, Assessment> getAssessmentMap() {
        return assessmentMap;
    }

    public void setAssessmentMap(HashMap<String, Assessment> assessmentMap) {
        this.assessmentMap = assessmentMap;
    }

    public HashMap<String, Integer> getCommentAssessmentMap2() {
        return commentAssessmentMap2;
    }

    public void setCommentAssessmentMap2(HashMap<String, Integer> commentAssessmentMap2) {
        this.commentAssessmentMap2 = commentAssessmentMap2;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public HashMap<String, String> getTimeMap() {
        return timeMap;
    }

    public void setTimeMap(HashMap<String, String> timeMap) {
        this.timeMap = timeMap;
    }

    public Comment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(Comment selectedComment) {
        this.selectedComment = selectedComment;
    }

    public HashMap<Comment, List<Comment>> getReplyMap() {
        return replyMap;
    }

    public void setReplyMap(HashMap<Comment, List<Comment>> replyMap) {
        this.replyMap = replyMap;
    }

    public HashMap<Comment, String> getRepliesTimeMap() {
        return repliesTimeMap;
    }

    public void setRepliesTimeMap(HashMap<Comment, String> repliesTimeMap) {
        this.repliesTimeMap = repliesTimeMap;
    }

    public Assessment getSelectedAssessment() {
        return selectedAssessment;
    }

    public void setSelectedAssessment(Assessment selectedAssessment) {
        this.selectedAssessment = selectedAssessment;
    }

    public EvaluationDao getEvaluationDao() {
        return evaluationDao;
    }

    public void setEvaluationDao(EvaluationDao evaluationDao) {
        this.evaluationDao = evaluationDao;
    }

    public HashMap<Assessment, Integer> getEvaluationMap() {
        return evaluationMap;
    }

    public void setEvaluationMap(HashMap<Assessment, Integer> evaluationMap) {
        this.evaluationMap = evaluationMap;
    }

    public HashMap<Assessment, Integer> getUserEvaluationMap() {
        return userEvaluationMap;
    }

    public void setUserEvaluationMap(HashMap<Assessment, Integer> userEvaluationMap) {
        this.userEvaluationMap = userEvaluationMap;
    }

}
