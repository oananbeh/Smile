/*
 * Copyright (c) 2010-2024 Haifeng Li. All rights reserved.
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
package smile.vision;

import java.io.IOException;
import smile.vision.transform.Transform;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haifeng Li
 */
public class ImageDatasetTest {

    public ImageDatasetTest() {
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void test() throws IOException {
        var transform = Transform.classification(384, 384);
        var data = new ImageDataset(4, "../imagenet/train", transform, ImageNet.folder2Target);
        assertEquals(1281167, data.size());
        var iter = data.iterator();
        assertEquals(true, iter.hasNext());
        var sample = iter.next();
        long[] dataShape = {4, 3, 384, 384};
        long[] targetShape = {4};
        assertArrayEquals(dataShape, sample.data().shape());
        assertArrayEquals(targetShape, sample.target().shape());
    }
}