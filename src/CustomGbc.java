import java.awt.GridBagConstraints;
import java.awt.Insets;

	/**
	* creates a custom instance of GridBagConstraints with specific constraints.
	* <p>
	* This class creates an instance of GridBagLayout with methods
	* that allow for quick initialization of certain settings.
	*/
	public class CustomGbc extends GridBagConstraints{
		private static final long serialVersionUID = 1L;
		
		CustomGbc(){
			this(new Insets(0,0,0,0));
		};
		
		CustomGbc(Insets insets){
			this(insets, 0, 0);
		};
		
		CustomGbc(Insets insets, double weightx, double weighty){
			this(insets, weightx, weighty, 1, 1, 0, 0);
		}
		
		CustomGbc(Insets insets, double weightx, double weighty, int width, int height, int gridx, int gridy){
			this.insets = insets;
			this.gridx = gridx;
			this.gridy = gridy;
			this.weightx = weightx;
			this.weighty = weighty;
			this.gridwidth = width;
			this.gridheight = height;
			this.fill = GridBagConstraints.BOTH;
		}
		
		public CustomGbc EditGbc(int gridx, int gridy) {
			return new CustomGbc(this.insets, this.weightx, this.weighty, this.gridwidth, this.gridheight, gridx, gridy);
		}
		
		public CustomGbc EditGbc(int gridx, int gridy, double weightx, double weighty) {
			return new CustomGbc(this.insets, weightx, weighty, this.gridwidth, this.gridheight, gridx, gridy);
		}
		
		public CustomGbc EditGbc(int gridx, int gridy, double weightx, double weighty, int width, int height) {
			return new CustomGbc(this.insets, weightx, weighty, width, height, gridx, gridy);
		}
		
		public CustomGbc EditGbc2(int gridx, int gridy, int width, int height) {
			return new CustomGbc(this.insets, weightx, weighty, width, height, gridx, gridy);
		}
	}
