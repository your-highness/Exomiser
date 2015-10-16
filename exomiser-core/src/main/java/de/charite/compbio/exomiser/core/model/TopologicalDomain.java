/*
 * The Exomiser - A tool to annotate and prioritize variants
 *
 * Copyright (C) 2012 - 2015  Charite Universitätsmedizin Berlin and Genome Research Ltd.
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

package de.charite.compbio.exomiser.core.model;

import java.util.Map;

/**
 * Represents a topological domain of a piece of chromatin
 *
 * @author Jules Jacobsen <jules.jacobsen@sanger.ac.uk>
 */
public class TopologicalDomain {

    private final int chromosome;
    private final int start;
    private final int end;
    private final Map<String, Integer> genes;

    public TopologicalDomain(int chromosome, int start, int end, Map<String, Integer> genes) {
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.genes = genes;
    }

    public int getChromosome() {
        return chromosome;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public Map<String, Integer> getGenes() {
        return genes;
    }

    public boolean containsPosition(VariantCoordinates variantCoordinates) {
        if (variantCoordinates.getChromosome() == chromosome) {
            int variantPosition = variantCoordinates.getPosition();
            return start <= variantPosition && end >= variantPosition;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopologicalDomain that = (TopologicalDomain) o;

        if (chromosome != that.chromosome) return false;
        if (start != that.start) return false;
        if (end != that.end) return false;
        return genes.equals(that.genes);

    }

    @Override
    public int hashCode() {
        int result = chromosome;
        result = 31 * result + start;
        result = 31 * result + end;
        result = 31 * result + genes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TopologicalDomain{" +
                "chromosome=" + chromosome +
                ", start=" + start +
                ", end=" + end +
                ", genes=" + genes +
                '}';
    }

}
