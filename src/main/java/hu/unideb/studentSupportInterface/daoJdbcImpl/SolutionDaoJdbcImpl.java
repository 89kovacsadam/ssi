/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.unideb.studentSupportInterface.daoJdbcImpl;

import hu.unideb.studentSupportInterface.dao.SolutionDao;
import hu.unideb.studentSupportInterface.mappers.SolutionMapper;
import hu.unideb.studentSupportInterface.model.Category;
import hu.unideb.studentSupportInterface.model.Language;
import hu.unideb.studentSupportInterface.model.Solution;
import hu.unideb.studentSupportInterface.model.User;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

/**
 *
 * @author Adam
 */
public class SolutionDaoJdbcImpl extends JdbcDaoSupport implements SolutionDao {

    @Override
    public Solution createSolution(Solution solution) {
        try {
            String sql = "insert into solution (uploader_id, language_id, definition, file, code, title) values (:uploader_id, :language_id, :definition, :file, :code, :title)";

            byte[] b = null;
            Blob blob = null;
            if (solution.getFile() != null) {
                b = IOUtils.toByteArray(solution.getFile());
                blob = new SerialBlob(b);
            }

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("file", b != null ? blob : null);
            params.addValue("uploader_id", solution.getUploader().getId());
            params.addValue("language_id", solution.getLanguage().getId());
            params.addValue("definition", solution.getDefinition());
            params.addValue("code", solution.getCode());
            params.addValue("title", solution.getTitle());

            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());
            KeyHolder key = new GeneratedKeyHolder();

            template.update(sql, params, key);

            solution.setId(key.getKey().intValue());

        } catch (SerialException ex) {
            Logger.getLogger(SolutionDaoJdbcImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SolutionDaoJdbcImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SolutionDaoJdbcImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return solution;

    }

    @Override
    public Solution updateSolution(Solution solution) {
        try {
            String sql = "update solution set uploader_id = :uploader_id, language_id = :language_id, definition = :definition, file = :file, code = :code, title = :title where id = :id";

            LobHandler lobhandler = new DefaultLobHandler();
            byte[] b = IOUtils.toByteArray(solution.getFile());

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("file", new SqlLobValue(solution.getFile(), b.length, lobhandler));
            params.addValue("uploader_id", solution.getUploader().getId());
            params.addValue("language_id", solution.getLanguage().getId());
            params.addValue("definition", solution.getDefinition());
            params.addValue("code", solution.getCode());
            params.addValue("id", solution.getId());
            params.addValue("title", solution.getTitle());

            NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getDataSource());

            template.update(sql, params);

        } catch (IOException ex) {
            Logger.getLogger(SolutionDaoJdbcImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return solution;
    }

    @Override
    public boolean deleteSolution(Solution solution) {
        String sql = "delete from solution where id = ?";

        getJdbcTemplate().update(sql, new Object[]{solution.getId()});

        return true;

    }

    @Override
    public List<Solution> getAllSolution() {
        String sql = "select * from solution";
        List<Solution> list = null;

        list = (List) getJdbcTemplate().query(sql, new SolutionMapper());

        return list;

    }

    @Override
    public Solution getSolutionById(int id) {
        String sql = "select * from solution where id = ?";
        List<Solution> list = null;

        list = (List) getJdbcTemplate().query(sql, new Object[]{id}, new SolutionMapper());

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        return null;

    }

    @Override
    public List<Solution> getSolutionsByUploader(User uploader) {
        String sql = "select * from solution where uploader_id = ?";
        List<Solution> list = null;

        list = (List) getJdbcTemplate().query(sql, new Object[]{uploader.getId()}, new SolutionMapper());

        return list;

    }

    @Override
    public List<Solution> getSolutionsByAssessor(User assessor) {
        String sql = "select s.id id, s.uploader_id uploader_id, s.definition definition, s.language_id language_id, s.time time, s.file file, s.code code, s.title title from solution s, assessment a where a.solution_id = s.id and a.assessor_id = ? group by s.id";
        String sql2 = "select s.id id, s.uploader_id uploader_id, s.definition definition, s.language_id language_id, s.time time, s.file file, s.code code, s.title title from solution s, distribution d where d.solution_id = s.id and d.assessor_id = ?";
        List<Solution> list = null;
        List<Solution> list2 = null;

        list = (List) getJdbcTemplate().query(sql, new Object[]{assessor.getId()}, new SolutionMapper());
        list2 = (List) getJdbcTemplate().query(sql2, new Object[]{assessor.getId()}, new SolutionMapper());

        list.addAll(list2);

        return list;

    }

    public List<Solution> getAssessedSolutionsByAssessor(User assessor, List<Category> catList, String pattern) {
        String sql = "select s.id id, s.uploader_id uploader_id, s.definition definition, s.language_id language_id, s.time time, s.file file, s.code code, s.title title "
                + "from (solution s, assessment a) left join solution_category c on s.id = c.solution_id "
                + "where a.solution_id = s.id and a.assessor_id = ?";

        int count = 1;

        if (catList != null && !catList.isEmpty()) {
            count += catList.size();
            sql = sql + " and (";
            for (Category c : catList) {
                sql += "c.category_id = ?";
                if (catList.indexOf(c) < catList.size() - 1) {
                    sql += " or ";
                }
            }
            sql += ")";
        }

        if (pattern != null && !pattern.isEmpty()) {
            pattern = "%" + pattern + "%";
            count += 2;
            sql += " and (lower(s.title) like ? or lower(s.definition) like ?)";
        }

        sql += " group by s.id order by s.time desc";

        Object[] params = new Object[count];
        int i = 0;

        params[i++] = assessor.getId();

        if (catList != null && !catList.isEmpty()) {
            for (Category c : catList) {
                params[i++] = c.getId();
            }
        }
        if (pattern != null && !pattern.isEmpty()) {
            params[i++] = pattern;
            params[i++] = pattern;
        }

        List<Solution> list = null;
        list = (List) getJdbcTemplate().query(sql, params, new SolutionMapper());

        return list;
    }

    public List<Solution> getSolutionsForAssessByAssessor(User assessor, List<Category> catList, String pattern) {
        String sql = "select s.id id, s.uploader_id uploader_id, s.definition definition, s.language_id language_id, s.time time, s.file file, s.code code, s.title title "
                + "from (solution s, distribution d) left join solution_category c on s.id = c.solution_id "
                + "where d.solution_id = s.id and d.assessor_id = ?";

        int count = 1;

        if (catList != null && !catList.isEmpty()) {
            count += catList.size();
            sql = sql + " and (";
            for (Category c : catList) {
                sql += "c.category_id = ?";
                if (catList.indexOf(c) < catList.size() - 1) {
                    sql += " or ";
                }
            }
            sql += ")";
        }

        if (pattern != null && !pattern.isEmpty()) {
            pattern = "%" + pattern + "%";
            count += 2;
            sql += " and (lower(s.title) like ? or lower(s.definition) like ?)";
        }

        sql += " group by s.id order by s.time desc";

        Object[] params = new Object[count];
        int i = 0;

        params[i++] = assessor.getId();

        if (catList != null && !catList.isEmpty()) {
            for (Category c : catList) {
                params[i++] = c.getId();
            }
        }
        if (pattern != null && !pattern.isEmpty()) {
            params[i++] = pattern;
            params[i++] = pattern;
        }

        List<Solution> list = null;
        list = (List) getJdbcTemplate().query(sql, params, new SolutionMapper());

        return list;
    }

    @Override
    public List<Solution> getSolutionsByCategory(Category category) {
        String sql = "select s.id id, s.uploader_id uploader_id, s.definition definition, s.language_id language_id, s.time time, s.file file, s.code code, s.title title from solution s, solution_category sc where sc.solution_id = s.id and sc.category_id = ?";
        List<Solution> list = null;

        list = (List) getJdbcTemplate().query(sql, new Object[]{category.getId()}, new SolutionMapper());

        return list;

    }

    @Override
    public List<Solution> getSolutionsByLanguage(Language language) {
        String sql = "select * from solution where language_id = ?";
        List<Solution> list = null;

        list = (List) getJdbcTemplate().query(sql, new Object[]{language.getId()}, new SolutionMapper());

        return list;

    }

    @Override
    public boolean addSolutionToAssessor(Solution solution, User assessor) {
        String sql = "insert into distribution (solution_id, assessor_id) values (?, ?)";

        getJdbcTemplate().update(sql, new Object[]{solution.getId(), assessor.getId()});

        return true;

    }

    @Override
    public boolean removeSolutionFromAssessor(Solution solution, User assessor) {
        String sql = "delete from distribution where solution_id = ? and assessor_id = ?";

        getJdbcTemplate().update(sql, new Object[]{solution.getId(), assessor.getId()});

        return true;
    }

    @Override
    public boolean addSolutionToCategory(Solution solution, Category category) {
        String sql = "insert into solution_category (solution_id, category_id) values (?, ?)";

        getJdbcTemplate().update(sql, new Object[]{solution.getId(), category.getId()});

        return true;

    }

    @Override
    public boolean removeSolutionFromCategory(Solution solution, Category category) {
        String sql = "delete from distribution where solution_id = ? and assessor_id = ?";

        getJdbcTemplate().update(sql, new Object[]{solution.getId(), category.getId()});

        return true;
    }

    public List<Solution> filterSolutions(List<Category> catList, String pattern, User uploader) {
        String sql = "select s.id id, s.uploader_id uploader_id, s.definition definition, s.language_id language_id, s.time time, s.file file, s.code code, s.title title "
                + "from solution s left join solution_category c on s.id = c.solution_id where s.id is not NULL";
        int count = 0;

        if (uploader != null) {
            sql += " and s.uploader_id =?";
            count += 1;
        }

        if (catList != null && !catList.isEmpty()) {
            count += catList.size();
            sql = sql + " and (";
            for (Category c : catList) {
                sql += "c.category_id = ?";
                if (catList.indexOf(c) < catList.size() - 1) {
                    sql += " or ";
                }
            }
            sql += ")";
        }

        if (pattern != null && !pattern.isEmpty()) {
            pattern = "%" + pattern + "%";
            count += 2;
            sql += " and (lower(s.title) like ? or lower(s.definition) like ?)";
        }

        sql += " group by s.id order by s.time desc";

        Object[] params = new Object[count];
        int i = 0;

        if (uploader != null) {
            params[i++] = uploader.getId();
        }

        if (catList != null && !catList.isEmpty()) {
            for (Category c : catList) {
                params[i++] = c.getId();
            }
        }
        if (pattern != null && !pattern.isEmpty()) {
            params[i++] = pattern;
            params[i++] = pattern;
        }

        List<Solution> list = (List) getJdbcTemplate().query(sql, params, new SolutionMapper());

        return list;

    }

}
