/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.monarchinitiative.exomiser.core.prioritisers.util;

import org.monarchinitiative.exomiser.core.model.Model;

import java.util.List;

/**
 * Interface for Services retrieving model data.
 * 
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public interface ModelService {

    List<Model> getHumanDiseaseModels();

    List<Model> getMouseGeneModels();

    List<Model> getFishGeneModels();
    
    
}