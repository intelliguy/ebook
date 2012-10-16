package a_test;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

class OutlineNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean areChildrenDefined = false;
	private int outlineNum;
	private int numChildren;

	public OutlineNode(int outlineNum, int numChildren) {
		this.outlineNum = outlineNum;
		this.numChildren = numChildren;
	}
	  
	@Override
	public boolean isLeaf() {
		return(false);
	}

	@Override
	public int getChildCount() {
		if (!areChildrenDefined)
			defineChildNodes();
		return(super.getChildCount());
	}

	private void defineChildNodes() {
	    // You must set the flag before defining children if you
	    // use "add" for the new children. Otherwise you get an infinite
	    // recursive loop, since add results in a call to getChildCount.
	    // However, you could use "insert" in such a case.
		areChildrenDefined = true;
		for(int i=0; i<numChildren; i++)
			add(new OutlineNode(i+1, numChildren));
	}

	@Override
	public String toString() {
		TreeNode parent = getParent();
		if (parent == null)
			return(String.valueOf(outlineNum));
		else
			return(parent.toString() + "." + outlineNum);
	}
}

public class DynamicTree extends JFrame {
  /**
	 * 
	 */
	private static final long serialVersionUID = -5635031635368042208L;

public static void main(String[] args) {
    int n = 5; // Number of children to give each node
    if (args.length > 0)
      try {
        n = Integer.parseInt(args[0]);
      } catch(NumberFormatException nfe) {
        System.out.println("Can't parse number; using default of " + n);
      }
    new DynamicTree(n);
  }
 
  public DynamicTree(int n) {
    super("Creating a Dynamic JTree");
    Container content = getContentPane();
    JTree tree = new JTree(new OutlineNode(1, n));
    content.add(new JScrollPane(tree), BorderLayout.CENTER);
    setSize(300, 475);
    setVisible(true);
  }
}