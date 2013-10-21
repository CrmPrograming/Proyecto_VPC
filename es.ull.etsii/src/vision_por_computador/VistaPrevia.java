package vision_por_computador;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;

@SuppressWarnings("serial")
public class VistaPrevia extends JComponent implements PropertyChangeListener {

  private ImageIcon thumbnail = null;
  private File file = null;
  
  public VistaPrevia(JFileChooser fc) {
    this.setPreferredSize(new Dimension(200, 100));
    fc.addPropertyChangeListener(this);
  }
  
  private void loadImage() throws IOException {
    if (file == null) {
      thumbnail = null;
      return;
    }
    
    FileSeekableStream stream = new FileSeekableStream(file.getAbsolutePath());
    TIFFDecodeParam decodeParam = new TIFFDecodeParam();
    decodeParam.setDecodePaletteAsShorts(true);
    ParameterBlock params = new ParameterBlock();
    params.add(stream);
    RenderedOp image1 = JAI.create("tiff", params);
    BufferedImage img = image1.getAsBufferedImage();
    
    ImageIcon tmpIcon = new ImageIcon(img);
    if (tmpIcon != null) {
      if (tmpIcon.getIconWidth() > 180) {
        thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(180, -1, Image.SCALE_DEFAULT));
      } else {
        thumbnail = tmpIcon;
      }
    }
  }
  
  public void propertyChange(PropertyChangeEvent e) {
    boolean update = false;
    String prop = e.getPropertyName();
    if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
      file = null;
      update = true;

  //If a file became selected, find out which one.
    } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
      file = (File) e.getNewValue();
      update = true;
    }

  //Update the preview accordingly.
    if (update) {
      thumbnail = null;
      if (isShowing()) {
        try {
          loadImage();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        repaint();
      }
    }
  }
  
  protected void paintComponent(Graphics g) {
    if (thumbnail == null) {
        try {
          loadImage();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    }
    if (thumbnail != null) {
        int x = getWidth()/2 - thumbnail.getIconWidth()/2;
        int y = getHeight()/2 - thumbnail.getIconHeight()/2;

        if (y < 0) {
            y = 0;
        }

        if (x < 5) {
            x = 5;
        }
        thumbnail.paintIcon(this, g, x, y);
    }
}

}
