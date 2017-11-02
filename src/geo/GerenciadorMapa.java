package geo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jxmapviewer.*;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

/**
 * Classe para gerenciar um mapa
 *
 * @author Marcelo Cohen
 */
public final class GerenciadorMapa {

    final JXMapKit jXMapKit;
    private WaypointPainter<MyWaypoint> pontosPainter;

    private static GeoPosition selCentro;
    private static GeoPosition selBorda;

    public enum FonteImagens {

        OpenStreetMap, VirtualEarth
    };

    /*
     * Cria um gerenciador de mapas, a partir de uma posição e uma fonte de imagens
     * 
     * @param centro centro do mapa
     * @param fonte fonte das imagens (FonteImagens.OpenStreetMap ou FonteImagens.VirtualEarth)
     */
    public GerenciadorMapa(GeoPosition centro, FonteImagens fonte) {
        jXMapKit = new JXMapKit();
        TileFactoryInfo info = null;
        if (fonte == FonteImagens.OpenStreetMap) {
            info = new OSMTileFactoryInfo();
        } else {
            info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        }
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        jXMapKit.setTileFactory(tileFactory);

        // Ajustando a opacidade do mapa (50%)
        jXMapKit.getMainMap().setAlpha(0.5f);

        // Ajustando o nível de zoom do mapa
        jXMapKit.setZoom(4);
        // Informando o centro do mapa
        jXMapKit.setAddressLocation(centro);
        // Indicando que não desejamos ver um marcador nessa posição
        jXMapKit.setAddressLocationShown(false);

        // Criando um objeto para "pintar" os pontos
        pontosPainter = new WaypointPainter<MyWaypoint>();

        // Criando um objeto para desenhar os pontos
        pontosPainter.setRenderer(new WaypointRenderer<MyWaypoint>() {

            @Override
            public void paintWaypoint(Graphics2D g, JXMapViewer viewer, MyWaypoint wp) {

                // Desenha cada waypoint como um pequeno círculo            	
                Point2D point = viewer.getTileFactory().geoToPixel(wp.getPosition(), viewer.getZoom());
                int x = (int) point.getX();
                int y = (int) point.getY();
                //g = (Graphics2D) g.create();
                g.setColor(wp.getColor());
                g.fill(new Ellipse2D.Float(x - 3, y - 3, 6, 6));
            }
        });

        // Criando um objeto para desenhar os elementos de interface
        // (círculo de seleção, etc)
        Painter<JXMapViewer> guiPainter = new Painter<JXMapViewer>() {
            public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
                if (selCentro == null || selBorda == null) {
                    return;
                }
                Point2D point = map.convertGeoPositionToPoint(selCentro);
                Point2D pont2 = map.convertGeoPositionToPoint(selBorda);
                int x = (int) point.getX();
                int y = (int) point.getY();
                int raio = (int) Math.sqrt(Math.pow(point.getX() - pont2.getX(), 2)
                        + Math.pow(point.getY() - pont2.getY(), 2));
                int r = raio;
                g.setColor(Color.RED);
                g.setStroke(new BasicStroke(2));
                g.draw(new Ellipse2D.Float(x - r, y - r, 2 * raio, 2 * raio));
                g.drawString(getRaio() + " metros", (int) pont2.getX() + 8, (int) pont2.getY() + 25);
                g.fill(new Ellipse2D.Float(x - 3, y - 3, 6, 6));
            }
        };

        /*
         Painter<JXMapViewer> textOverlay = new Painter<JXMapViewer>() {
         public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
         g.setPaint(new Color(0,0,0,150));
         g.fillRoundRect(50, 10, 182 , 30, 10, 10);
         g.setPaint(Color.WHITE);
         g.drawString("Images provided by NASA", 50+10, 10+20);
         }
         };
         */
        // Um CompoundPainter permite combinar vários painters ao mesmo tempo...
        CompoundPainter cp = new CompoundPainter();
        cp.setPainters(pontosPainter, guiPainter);

        jXMapKit.getMainMap().setOverlayPainter(cp);

        selCentro = null;
        selBorda = null;
    }

    /*
     * Informa a localização do ponto central da região
     * @param ponto central
     */
    public void setSelecaoCentro(GeoPosition sel) {
        this.selCentro = sel;
    }

    public Parada ParadaMaisProxima() throws IOException {
        EstruturaDeDados estrutura = new EstruturaDeDados();
        ArrayList<Parada> lista = estrutura.listaParadas;
        double menorDistancia = getRaio();
        Parada parada = null;
        for (int i = 0; i < lista.size(); i++) {
            double distancia = AlgoritmosGeograficos.haversine(
                    selCentro.getLatitude(),
                    selCentro.getLongitude(),
                    lista.get(i).getLatitude(),
                    lista.get(i).getLongitude()
            );
            if (distancia < getDistancia() && distancia < menorDistancia) {
                parada = lista.get(i);
            }
        }
        if (parada == null) {
            throw new IllegalArgumentException("Nenhuma parada encontrada dentro de raio");
        }
        return parada;
    }

    public ListDoubleLinked<Parada> conjuntoDeParadasDentroDeRaio() throws IOException {
        EstruturaDeDados estrutura = new EstruturaDeDados();
        ArrayList<Parada> lista = estrutura.listaParadas;
        ListDoubleLinked<Parada> novaLista = new ListDoubleLinked<>();
        for (int i = 0; i < lista.size(); i++) {
            double distancia = AlgoritmosGeograficos.haversine(
                    selCentro.getLatitude(),
                    selCentro.getLongitude(),
                    lista.get(i).getLatitude(),
                    lista.get(i).getLongitude()
            );
            if (distancia < getDistancia()) {
                novaLista.add(lista.get(i));
            }
        }
        if (novaLista.size() == 0) {
            throw new IllegalArgumentException("Nenhuma parada encontrada dentro de raio");
        }
        return novaLista;
    }

    public Map<Integer, Linha> getLinhasDeConjuntoParadas() throws IOException {
        EstruturaDeDados estrutura = new EstruturaDeDados();
        ListDoubleLinked<Parada> conjuntoParada = conjuntoDeParadasDentroDeRaio();
        ListDoubleLinked<Linha> listaAux = new ListDoubleLinked<>();
        Map<Integer, Linha> linhaDeOnibus = new HashMap<Integer, Linha>();

        for (int i = 0; i < conjuntoParada.size(); i++) {
            listaAux = estrutura.getLinhasDeOnibusPorParada(conjuntoParada.get(i).getIdParada());
            for (int j = 0; j < listaAux.size(); j++) {
                linhaDeOnibus.put(listaAux.get(j).getIdLinha(), listaAux.get(j));
            }
        }
        if (linhaDeOnibus.size() == 0) {
            throw new IllegalArgumentException("Nenhuma linha encontrada dentro de raio");
        }
        return linhaDeOnibus;
    }

    public GeoPosition getSelecaoCentro() {
        return selCentro;
    }

    /*
     * Informa a localização de um ponto da borda da região
     * Utilizamos isso para definir o raio da região e desenhar o círculo 
     * @param ponto da borda
     */
    public void setSelecaoBorda(GeoPosition sb) {
        this.selBorda = sb;
    }

    // Retorna o raio da região selecionada (em metros)
    public int getRaio() {
        return (int) (AlgoritmosGeograficos.calcDistancia(selBorda, selCentro) * 1000);
    }

    public double getDistancia() {
        return (AlgoritmosGeograficos.calcDistancia(selBorda, selCentro));
    }

    /*
     * Informa os pontos a serem desenhados (precisa ser chamado a cada vez que mudar)
     * @param lista lista de pontos (objetos MyWaypoint)
     */
    public void setPontos(List<MyWaypoint> lista) {
        // Criando um conjunto de pontos
        Set<MyWaypoint> pontos = new HashSet<MyWaypoint>(lista);
        // Informando o conjunto ao painter
        pontosPainter.setWaypoints(pontos);
    }

    /*
     * Retorna a referência ao objeto JXMapKit, para ajuste de parâmetros (se for o caso)
     * @returns referência para objeto JXMapKit em uso
     */
    public JXMapKit getMapKit() {
        return jXMapKit;
    }

}
