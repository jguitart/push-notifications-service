package org.jguitart;

import io.quarkus.test.junit.NativeImageTest;
import org.jguitart.notification.push.resouce.applicationconfig.CreateApplicationConfigResourceTest;

@NativeImageTest
public class NativeApplicationConfigResourceTest extends CreateApplicationConfigResourceTest {

    // Execute the same tests but in native mode.
}