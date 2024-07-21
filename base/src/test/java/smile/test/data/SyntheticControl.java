/*
 * Copyright (c) 2010-2021 Haifeng Li. All rights reserved.
 *
 * Smile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Smile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Smile.  If not, see <https://www.gnu.org/licenses/>.
 */

package smile.test.data;

import org.apache.commons.csv.CSVFormat;
import smile.data.CategoricalEncoder;
import smile.data.DataFrame;
import smile.io.Read;
import smile.util.Paths;

/**
 *
 * @author Haifeng
 */
public class SyntheticControl {

    public static DataFrame data;
    public static double[][] x;

    static {
        try {
            CSVFormat format = CSVFormat.Builder.create().setDelimiter(' ').build();
            data = Read.csv(Paths.getTestData("clustering/synthetic_control.data"), format);
            x = data.toArray(false, CategoricalEncoder.DUMMY);
        } catch (Exception ex) {
            System.err.println("Failed to load 'synthetic_control': " + ex);
            System.exit(-1);
        }
    }
}
