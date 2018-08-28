
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StopWatch extends JPanel
{
	public Timer myTimer1;
	public static final int ONE_SEC = 1000;   //time step in milliseconds
	public static final int TENTH_SEC = 100;

	private Font myClockFont;

	
	private JLabel timeLbl;
	private JPanel topPanel;

	private int clockTick;  	//number of clock ticks; tick can be 1.0 s or 0.1 s
	private double clockTime;  	//time in seconds
	private String clockTimeString;


	public StopWatch()
	{
		clockTick = 0;  		//initial clock setting in clock ticks
		clockTime = ((double)clockTick)/10.0;

		clockTimeString = new Double(clockTime).toString();
		myClockFont = new Font("Serif", Font.PLAIN, 50);

		timeLbl = new JLabel();
		timeLbl.setFont(myClockFont);
		timeLbl.setText(clockTimeString);



		myTimer1 = new Timer(TENTH_SEC, new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			clockTick++;
			clockTime = ((double)clockTick)/10.0;
			clockTimeString = new Double(clockTime).toString();
			timeLbl.setText(clockTimeString);
			//System.out.println(clockTime);
		    }
		});
		topPanel = new JPanel();
		topPanel.setBackground(Color.orange);
		topPanel.add(timeLbl);

		this.setLayout(new BorderLayout());

		add(topPanel, BorderLayout.CENTER);

	}//end of StopWatch constructor


	public void start() 
	{
		this.myTimer1.start();
	}
	
	public void stop() 
	{
		this.myTimer1.stop();
	}
	
	public void reset() 
	{
		this.clockTick = 0;
		this.clockTime = ((double)clockTick)/10.0;
		this.clockTimeString = new Double(clockTime).toString();
		this.timeLbl.setText(clockTimeString);
	}

	public String getTime() 
	{
		return this.clockTimeString;
	}
	//end of launchClock



}