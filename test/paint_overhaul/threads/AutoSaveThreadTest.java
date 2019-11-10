/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint_overhaul.threads;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import paint_overhaul.constant.DrawingTools;

/**
 *
 * @author Dylan
 */
public class AutoSaveThreadTest {
    
    public AutoSaveThreadTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testTimeElapsedZero() throws InterruptedException{
        AutoSaveThread instance = new AutoSaveThread(60);
        
        assertEquals(instance.getTimeElapsed(), 0);
    }
    
    @Test
    public void testLogFileLocation(){
        AutoSaveThread instance = new AutoSaveThread(60);
        File file = new File("src/paint_overhaul/logs/log.txt");
        
        assertEquals(file, instance.getLogFile());
    }
    
    @Test
    public void testDrawingTools(){
        String instance = "PENCIL";
        assertEquals(instance, DrawingTools.PENCIL.toString());
        instance = "LINE";
        assertEquals(instance, DrawingTools.LINE.toString());
        instance = "RECTANGLE";
        assertEquals(instance, DrawingTools.RECTANGLE.toString());
    }
}
