/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geo;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author Cohen
 */
public class JanelaConsulta extends javax.swing.JFrame {

    private GerenciadorMapa gerenciador;
    private EventosMouse mouse;

    private JPanel painelLateral;
    private JPanel painelMapa;
    private JLabel consultas;
    private JLabel textLinhasDeParada;
    private JLabel textParadasDeLinha;
    private JLabel textLinhasRegiao;
    private JLabel textParadaProxima;
    private JLabel textHistogramaParadas;
    private JLabel textIntegrantes;
    private JTextField linhasDeParada;
    private JTextField paradasDeLinha;
    private JButton butLinhasRegiao;
    private JButton butParadaProxima;
    private JButton butLinhasDeParada;
    private JButton butParadasDeLinha;
    private JButton butHistogramaParadas;
    private JButton butIntegrantes;
    private JFrame tela;
    private JTextArea textArea;
    private JScrollPane telaScroll;

    /**
     * Creates new form JanelaConsulta
     */
    public JanelaConsulta() throws IOException {
        super("Análise das Linhas de Ônibus de Porto Alegre");

        GeoPosition poa = new GeoPosition(-30.05, -51.18);
        gerenciador = new GerenciadorMapa(poa, GerenciadorMapa.FonteImagens.VirtualEarth);
        mouse = new EventosMouse();
        gerenciador.getMapKit().getMainMap().addMouseListener(mouse);
        gerenciador.getMapKit().getMainMap().addMouseMotionListener(mouse);

        painelMapa = new JPanel();
        painelMapa.setLayout(new BorderLayout());
        painelMapa.setPreferredSize(new Dimension(800, 600));
        painelMapa.setMinimumSize(new Dimension(800, 600));
        painelMapa.add(gerenciador.getMapKit());//, BorderLayout.CENTER);

        getContentPane().add(painelMapa, BorderLayout.CENTER);

        painelLateral = new JPanel();
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelLateral.setPreferredSize(new Dimension(300, 300));
        painelLateral.setLayout(new GridLayout(20, 1, 0, 5));
        getContentPane().add(painelLateral, BorderLayout.BEFORE_LINE_BEGINS);

        /**
         * Consultas
         */
        consultas = new JLabel("Consultar:");

        painelLateral.add(consultas);

        /**
         * Exibir as linhas de ônibus que passam em uma determinada parada
         */
        textLinhasDeParada = new JLabel("1) Linhas de ônibus de uma determinada Parada");
        linhasDeParada = new JTextField();
        butLinhasDeParada = new JButton();
        butLinhasDeParada.addActionListener(e -> consultaParada(e));
        butLinhasDeParada.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        linhasDeParada.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        butLinhasDeParada.setText("Consultar");
        painelLateral.add(textLinhasDeParada);
        painelLateral.add(linhasDeParada);
        painelLateral.add(butLinhasDeParada);

        /**
         * Exibir todas as paradas de uma linha de ônibus
         */
        textParadasDeLinha = new JLabel("2) Paradas de uma determinada Linha de ônibus");
        paradasDeLinha = new JTextField();
        butParadasDeLinha = new JButton();
        paradasDeLinha.addActionListener(e -> consultaParada(e));
        butParadasDeLinha.addActionListener(e -> consultaParada(e));
        butParadasDeLinha.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        paradasDeLinha.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        butParadasDeLinha.setText("Consultar");
        painelLateral.add(textParadasDeLinha);
        painelLateral.add(paradasDeLinha);
        painelLateral.add(butParadasDeLinha);

        /**
         * Exibir as linhas de ônibus que passam em um conjunto de paradas
         * selecionadas
         */
        textLinhasRegiao = new JLabel("3) Linhas de ônibus que passam em uma Região");
        butLinhasRegiao = new JButton();
        butLinhasRegiao.addActionListener(e -> consultaParada(e));
        butLinhasRegiao.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        butLinhasRegiao.setText("Consultar");
        painelLateral.add(textLinhasRegiao);
        painelLateral.add(butLinhasRegiao);

        /**
         * Exibir a parada mais próxima de uma determinada localização no mapa
         */
        textParadaProxima = new JLabel("4) Parada mais Próxima de uma localização");
        butParadaProxima = new JButton();
        butParadaProxima.addActionListener(e -> consultaParada(e));
        butParadaProxima.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        butParadaProxima.setText("Consultar");
        painelLateral.add(textParadaProxima);
        painelLateral.add(butParadaProxima);

        /**
         * Exibir um histograma do número de paradas por linha
         */
        textHistogramaParadas = new JLabel("5) Histograma do número de Paradas por Linha");
        butHistogramaParadas = new JButton();
        butHistogramaParadas.addActionListener(e -> consultaParada(e));
        butHistogramaParadas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        butHistogramaParadas.setText("Consultar");
        painelLateral.add(textHistogramaParadas);
        painelLateral.add(butHistogramaParadas);

        /**
         * Exibir os nomes dos integrantes
         */
        textIntegrantes = new JLabel("- Nomes dos Integrantes");
        butIntegrantes = new JButton();
        butIntegrantes.addActionListener(e -> consultaParada(e));
        butIntegrantes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        butIntegrantes.setText("Integrantes");
        painelLateral.add(textIntegrantes);
        painelLateral.add(butIntegrantes);

        butLinhasDeParada.addActionListener(new ActionListener() {
            ListDoubleLinked<Linha> linhas = new ListDoubleLinked<>();
            EstruturaDeDados estrutura = new EstruturaDeDados();

            public void actionPerformed(ActionEvent e) {
                int parada = Integer.parseInt(linhasDeParada.getText());
                linhas = estrutura.getLinhasDeOnibusPorParada(parada);
                textArea = new JTextArea(linhas.toString());
                tela = new JFrame("Resultado da Consulta");
                telaScroll = new JScrollPane(textArea);
                tela.setVisible(true);
                tela.setSize(800, 600);
//                tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                telaScroll.setAutoscrolls(true);
                telaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                tela.add(telaScroll);
            }
        });

        butParadasDeLinha.addActionListener(new ActionListener() {
            ListDoubleLinked<Parada> paradas = new ListDoubleLinked<>();
            EstruturaDeDados estrutura = new EstruturaDeDados();

            public void actionPerformed(ActionEvent e) {
                int linha = Integer.parseInt(paradasDeLinha.getText());
                paradas = estrutura.getParadasPorLinhaDeOnibus(linha);
                textArea = new JTextArea(paradas.toString());
                tela = new JFrame("Resultado da Consulta");
                telaScroll = new JScrollPane(textArea);
                tela.setVisible(true);
                tela.setSize(800, 600);
//                tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                telaScroll.setAutoscrolls(true);
                telaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                tela.add(telaScroll);
            }
        });

        butLinhasRegiao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HashMap<Integer, Linha> lista = new HashMap<>();
                ListDoubleLinked<Linha> linha = new ListDoubleLinked<>();
                try {
                    lista = (HashMap<Integer, Linha>) gerenciador.getLinhasDeConjuntoParadas();
                    Iterator it = lista.entrySet().iterator();
                    while (it.hasNext()) {
                        HashMap.Entry pairs = (HashMap.Entry) it.next();
                        linha.add((Linha) pairs.getValue());
                    }
                    textArea = new JTextArea(linha.toString());

                    tela = new JFrame("Resultado da Consulta");
                    telaScroll = new JScrollPane(textArea);
                    tela.setVisible(true);
                    tela.setSize(800, 600);
//                    tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    telaScroll.setAutoscrolls(true);
                    telaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                    tela.add(telaScroll);

                    System.out.println(lista.toString());
                } catch (IOException ex) {
                    Logger.getLogger(JanelaConsulta.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        butParadaProxima.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Parada parada = gerenciador.ParadaMaisProxima();
                    JOptionPane.showMessageDialog(rootPane, parada.getCodigo(), "Código da parada mais próxima", WIDTH);
                } catch (IOException ex) {
                    Logger.getLogger(JanelaConsulta.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        /**
         * Exibi os nomes dos integrantes do grupo
         */
        butIntegrantes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(rootPane, "Camila Moser, Franciely Pedroso e Lucas Freitas");
            }
        });
        
        butHistogramaParadas.addActionListener(new ActionListener(){;
        public void actionPerformed(ActionEvent e){
            Histogram histo = null;
            try {
                histo = new Histogram();
            } catch (IOException ex) {
                Logger.getLogger(JanelaConsulta.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            histo.setBounds(10, 10, 500, 500);
            histo.setTitle("Histograma");
            histo.setVisible(true);
        }
    });
        
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Exemplo de como obter as coordenadas do mouse e raio
    private void consultaParada(ActionEvent evt) {

        // ObtÃ©m o ponto clicado no mapa
        GeoPosition pos = gerenciador.getSelecaoCentro();
        // ObtÃ©m o raio escolhido pelo usuÃ¡rio (em metros)
//        double raio = gerenciador.getRaio();

        // Como adicionar pontos na tela: cria-se uma lista...
        List<MyWaypoint> lstPoints = new ArrayList<>();

        // .. e adiciona-se objetos MyWaypoint nela   
        // Neste exemplo, usamos o prÃ³prio ponto clicado pelo usuÃ¡rio
        lstPoints.add(new MyWaypoint(Color.BLUE, 1, pos));

        // Finamente, envia esta lista ao gerenciador
        gerenciador.setPontos(lstPoints);

        // Redesenha a janela
        this.repaint();
    }

    // Classe interna para gerenciar os eventos do mouse (click e drag)
    private class EventosMouse extends MouseAdapter {

        private int lastButton = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            JXMapViewer mapa = gerenciador.getMapKit().getMainMap();
            GeoPosition loc = mapa.convertPointToGeoPosition(e.getPoint());
//    		System.out.println(loc.getLatitude()+", "+loc.getLongitude());
            lastButton = e.getButton();
            if (lastButton == MouseEvent.BUTTON3) {
                gerenciador.setSelecaoCentro(loc);
                gerenciador.setSelecaoBorda(loc);
                //gerenciador.getMapKit().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                gerenciador.getMapKit().repaint();
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (lastButton == MouseEvent.BUTTON3) {
                JXMapViewer mapa = gerenciador.getMapKit().getMainMap();
                gerenciador.setSelecaoBorda(mapa.convertPointToGeoPosition(e.getPoint()));
                gerenciador.getMapKit().repaint();
            }
        }
    }
}
