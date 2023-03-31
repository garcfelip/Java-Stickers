import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.STRING;
import javax.swing.Spring;

public class GeradorDeFigurinhas {

    public void cria(InputStream inputStream, String nomeArquivo, String texto) throws Exception {

        // leitura da imagem
        //InputStream inputStream = new FileInputStream(new File("entrada/filme-maior.jpg"));
        //InputStream inputStream = new URL("https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@.jpg").openStream();

        BufferedImage imagemOriginal = ImageIO.read(inputStream);
        
        // cria nova imagem em memória com transparência e com tamanho novo 
        int largura = imagemOriginal.getWidth();
        int altura = imagemOriginal.getHeight();
        int novaAltura = altura + 200;

        BufferedImage novaImage = new BufferedImage(largura, novaAltura, BufferedImage.TRANSLUCENT); 
        
        // copiar a imagem original para novo imagem (em memória)
        Graphics2D graphics = (Graphics2D) novaImage.getGraphics();
        graphics.drawImage(imagemOriginal, 0, 0, null);

        // configurar fonte
        var fonte = new Font("Impact", Font.BOLD, 80);
        graphics.setColor(Color.YELLOW);
        graphics.setFont(fonte);
        
        // escrever uma frase na nova imagem 
        // var texto = "TOPZEIRA";
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle2D retangulo = fontMetrics.getStringBounds(texto, graphics);
        int larguraTexto = (int) retangulo.getWidth();
        int posicaoTextoX = (largura - larguraTexto) / 2;
        int posicaoTextoY = novaAltura - 100;

        graphics.drawString(texto, posicaoTextoX,posicaoTextoY);

        FontRenderContext fontRenderContext = graphics.getFontRenderContext();
        var textLayout = new TextLayout(texto, fonte, fontRenderContext);

        Shape outLine = textLayout.getOutline(null);
        AffineTransform transform = graphics.getTransform();
        transform.translate(posicaoTextoX, posicaoTextoY);
        graphics.setTransform(transform);

        var outlineStroke = new BasicStroke(largura * 0.004f);
        graphics.setStroke(outlineStroke);

        graphics.setColor(Color.BLACK);
        graphics.draw(outLine);
        graphics.setClip(outLine);



        // escrever a nova imagem em um arquivo 
        ImageIO.write(novaImage, "png", new File(nomeArquivo));
    }
    
}
