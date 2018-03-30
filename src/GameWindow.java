import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GameWindow {

	int wWidth;
	int wHeight;
	boolean[][] grid;
	
	public  void setGrid( boolean[][] _grid )
	{
		grid = _grid;
	}
	
	
	public GameWindow( boolean[][] _grid, int windowWidth, int windowHeight )
    {
		grid 	= _grid;
		wWidth 	= windowWidth;
		wHeight = windowHeight;
		
		
    	JFrame frame = buildFrame();

        JPanel pane = new JPanel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -3540614445002752114L;

			@Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            	g.setColor( Color.BLACK );
                g.fillRect(0, 0, wWidth, wHeight);
                drawGrid( g );
            }
        };



        frame.add(pane);
    }
    

    private JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize( wWidth, wHeight );
        frame.setVisible(true);
        return frame;
    }
    
    public void drawGrid( Graphics g )
    {
    	if( grid == null )
    		return;
    	g.setColor( Color.WHITE );
    	
    	int gridWidth 	= grid[0].length;
    	int gridHeight 	= grid.length;
    	
    	for( int x = gridWidth; x < wWidth; x+=gridWidth )
    	{
    		g.drawLine( x, 0, x, wHeight );
    	}
    	
    	for( int y = gridHeight; y < wHeight; y+=gridWidth )
    	{
    		g.drawLine( 0, y, wWidth, y );
    	}
    	
    	for( int x = 0; x < gridWidth; x++ )
    	{
    		for( int y = 0; y < gridHeight; y++ )
    		{
    			if( grid[y][x] )
    			{
    				g.fillRect( (x)*gridWidth, y*gridHeight, gridWidth, gridHeight );
    			}
    		}
    	}
    }
	
}
