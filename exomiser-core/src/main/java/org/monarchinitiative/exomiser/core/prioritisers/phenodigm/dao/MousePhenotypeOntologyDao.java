/*
 * The Exomiser - A tool to annotate and prioritize variants
 *
 * Copyright (C) 2012 - 2016  Charite Universitätsmedizin Berlin and Genome Research Ltd.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.monarchinitiative.exomiser.core.prioritisers.phenodigm.dao;

import org.monarchinitiative.exomiser.core.prioritisers.phenodigm.PhenotypeMatch;
import org.monarchinitiative.exomiser.core.prioritisers.phenodigm.PhenotypeTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

/**
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
@Repository
public class MousePhenotypeOntologyDao implements OntologyDao {

    private static final Logger logger = LoggerFactory.getLogger(MousePhenotypeOntologyDao.class);

    private final DataSource dataSource;

    @Autowired
    public MousePhenotypeOntologyDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Set<PhenotypeTerm> getAllTerms() {
        String query = "SELECT mp_id as id, mp_term as term FROM mp";
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ontologyTermsStatement = connection.prepareStatement(query);
                ResultSet rs = ontologyTermsStatement.executeQuery()) {

            return OntologyDaoResultSetProcessor.processOntologyTermsResultSet(rs);
            
        } catch (SQLException e) {
            logger.error("Unable to execute query '{}' for MPO terms", query, e);
        }
        return Collections.emptySet();
    }

    @Override
    public Set<PhenotypeMatch> getPhenotypeMatchesForHpoTerm(PhenotypeTerm hpoTerm) {
        String mappingQuery = "SELECT simj, ic, score, mp_id AS hit_id, mp_term AS hit_term, lcs_id, lcs_term FROM hp_mp_mappings WHERE hp_id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement ps = PreparedStatementSetter.prepareStatement(connection, mappingQuery, setter -> setter
                        .setString(1, hpoTerm.getId()));
                ResultSet rs = ps.executeQuery()) {

            return OntologyDaoResultSetProcessor.processOntologyTermMatchResultSet(rs, hpoTerm);
            
        } catch (SQLException e) {
            logger.error("Unable to execute query '{}' for HP-MP match terms", mappingQuery, e);
        }
        return Collections.emptySet();
    }

}
