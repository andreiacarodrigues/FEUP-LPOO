package org.feup.lcom.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por guardar os highscores. Implementa a interface Serializable, que permite a sua posterior serialização.
 * É apenas cosntituida por um HahMap que tem como chave o Integer correspondente ao score, e value o nome do player que atingiu esse score.
 * O HashMap tem como valor máximo 10 pontuações.
 */
public class Highscores implements Serializable {

    private Map<Integer, String > highscores;

    /**
     * Construtor da classe Highscores.
     * Apenas inicializa o HashMap.
     */
    public Highscores(){
       highscores = new HashMap<Integer, String>();
    }

    /**
     * Funcao que atualiza os Highscores. Recebe um novo score e o nome correspondente.
     * De seguida adiciona aos highscores, ordena pelo maior score, e se o size ficar maior que 10, elimina o ultimo (score mais baixo).
     * @param nome Nome do player.
     * @param score Score atingido pelo player.
     */
    public void updateHighscores(String nome, int score) {

        //adiciona ao arryList
        highscores.put(score,nome);

        //sort
        Map<Integer, String> sorted = sortByKeys(highscores);
        highscores = sorted;

        //elimina o ultimo
        if(highscores.size() > 10){

            Iterator<Integer> keySetIterator = highscores.keySet().iterator();
            Integer key = null;

            while(keySetIterator.hasNext())
                key = keySetIterator.next();

            highscores.remove(key);
        }
    }

    /**
     * Ordenador. Classe estatica que ordena o HashMap pelo maior até ao menor score.
     * Para isso é necessario o auxilio de uma linkedLisr que guardara as chaves.
     * Depois e usado um sort da Collection.
     * Por ultimo é necessario criar um novo HashMap que recebe as chaves ordenadas, e vai buscar ao antigo os respetivos valores.
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByKeys(Map<K,V> map){
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys, Collections.<K>reverseOrder());

        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
        for(K key: keys){
            sortedMap.put(key, map.get(key));
        }

        return sortedMap;
    }

    /**
     * Metodo que devolve o HashMap em forma de string
     * @return
     */
    public String toString(){

        String text = null;
        int i = 1;

        Iterator<Integer> keySetIterator = highscores.keySet().iterator();

        while(keySetIterator.hasNext()){
            Integer key = keySetIterator.next();
            if(i == 1)
                text = i + " : " + key + "  " + highscores.get(key) + "\n";
            else
                text += i + " : " + key + "  " + highscores.get(key) + "\n";
            i++;
        }

        return text;
    }
}
