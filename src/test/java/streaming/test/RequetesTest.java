/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package streaming.test;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import streaming.entity.Film;

/**
 *
 * @author admin
 */
public class RequetesTest {

    @Test //Vérifier le titre du film d'id 4
    public void Test01() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        //cree un objet query contenant la equete SL
        Query q = em.createQuery("SELECT f FROM Film f WHERE  f.id=4");
        //execute la requete en db et envoie un seul film
        Film f = (Film) q.getSingleResult();
        //assertion su le titre
        Assert.assertEquals("Fargo", f.getTitre());

    }

    @Test //Vérifier le nombre de films
    public void Test03() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT(f) FROM Film f").getSingleResult();
        Assert.assertEquals(4L, l);
    }

    @Test //Année de prod mini de nos films
    public void Test05() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        int a = (int) em.createQuery("SELECT MIN(f.annee) FROM Film f").getSingleResult();
        Assert.assertEquals(1968, a);// ou 
    }

    @Test //Nombre de liens du film 'Big Lebowski'
    public void Test07() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long la = (long) em.createQuery("SELECT COUNT(la) FROM Film f JOIN f.liens la WHERE f.titre LIKE '%Big Lebowski%'").getSingleResult();
        Assert.assertEquals(1L, la);
    }

    @Test //Nombre de films réalisés par Polanski
    public void Test09() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT (f) FROM Film f JOIN f.realisateurs r WHERE r.nom = 'Polanski'").getSingleResult();
        //long fr = (long) em.createQuery("SELECT COUNT(fr) FROM Film f JOIN f.realisateurs r WHERE r.nom= 'Polanksi'").getSingleResult(); // LIKE '%olanki%'").getSingleResult();
        //long fr = (long) em.createQuery("SELECT fr FROM Real_Film fr WHERE fr.realisateurs_id=1 ").getSingleResult();
        Assert.assertEquals(2L, l);
    }

    @Test //Nombre de films interprétés par Polanski
    public void Test011() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long fr = (long) em.createQuery("SELECT COUNT(f) FROM Film f JOIN f.acteurs a WHERE a.nom LIKE '%olanski%'").getSingleResult();
        Assert.assertEquals(1L, fr);
    }

    @Test //Nombre de films à la fois interprétés et réalisés par polanski
    public void Test013() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        //long l = (long) em.createQuery("SELECT COUNT (f) FROM Film f JOIN f.realisateurs r WHERE r.nom = 'Polanski' INTERSECT SELECT COUNT(f) FROM Film f JOIN f.acteurs a WHERE a.nom LIKE '%olanki%'").getSingleResult();
        long l = (long) em.createQuery("SELECT COUNT (f) FROM Film f JOIN f.realisateurs r JOIN f.acteurs a WHERE a.nom LIKE '%olanski%' AND r.nom LIKE '%olanski%'").getSingleResult();

        Assert.assertEquals(1L, l);
    }

    @Test //Le titre du film d'horreur anglais réalisé par roman polanski
    public void Test015() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        // String titr= (String) em.createQuery("SELECT f FROM Film f WHERE f.genre.nom=’Horreur’ AND SELECT (f) FROM Film f JOIN f.realisateurs r WHERE r.nom = 'Polanski' AND Select (f) FROM Pays p WHERE p.id=1").getSingleResult();
        String titr = (String) em.createQuery("SELECT f.titre FROM Film f JOIN f.genre g JOIN f.realisateurs r JOIN f.pays p WHERE g.id=1 AND  r.nom = 'Polanski' AND  p.id=1").getSingleResult();
        Assert.assertEquals("Le bal des vampires", titr);

    }

    @Test //Le nombre de films réalisés par joel coen
    public void Test017() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT (f) FROM Film f JOIN f.realisateurs r WHERE r.nom='Coen' AND r.prenom='Joel'").getFirstResult();
        Assert.assertEquals(0L, l);
    }

    @Test //Le nombre de films réalisés par les 2 frères coen
    public void Test019() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT (f) FROM Film f JOIN f.realisateurs r WHERE r.nom='Coen'").getSingleResult();
        Assert.assertEquals(4L, l);
    }

    @Test //Le nombre de films des frères coen interprétés par Steve Buscemi
    public void Test021() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT (DISTINCT f) FROM Film f JOIN f.realisateurs r JOIN f.acteurs a WHERE r.nom='Coen' AND a.nom='Buscemi'").getSingleResult();
        Assert.assertEquals(2L, l);
    }

    @Test //Le nombre de films des frères coen interprétés par Steve Buscemi
    public void Test021corige1() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        //List<Films> films = em.em.createQuery(" etc...
        List l = (List) em.createQuery("SELECT f FROM Film f JOIN f.realisateurs r WHERE r.nom='Coen' AND r.nom='Ethan' "
                + "INTERSECT "
                + "SELECT f FROM Film f JOIN f.realisateurs r WHERE r.nom='Coen' AND r.nom='Joel' "
                + "INTERSECT "
                + "SELECT f FROM Film f JOIN f.acteurs a WHERE a.nom='Buscemi' "
        ).getResultList();
        Assert.assertEquals(0L, l.size());
    }

    //@Test //Le nombre de films des frères coen interprétés par Steve Buscemi
    public void Test021corige2() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        String s = "SELECT COUNT(DISTINCT f) FROM Film f JOIN f.realisateurs r1 JOIN f.realisateurs r2 JOIN f.acteurs a  "
                + "WHERE r1.prenom='Joel' AND r1.nom='Coen' AND r2.prenom='Ethan' AND r2.nom='Coen' AND a.nom='Buscemi'";
    }

    @Test //Le nombre de films policiers des frères Coen interprétés par Steve Buscemi
    public void Test023() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT (f) FROM Film f JOIN f.realisateurs r JOIN f.acteurs a JOIN f.genre g WHERE r.nom='Coen' AND a.nom='Buscemi' AND g.nom='Policier'").getSingleResult();
        Assert.assertEquals(2L, l);
    }

    @Test //Le nombre de saisons de la série Dexter
    public void Test025() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT (s) FROM Saison s JOIN s.serie s2 WHERE s2.titre='Dexter'").getSingleResult();
        Assert.assertEquals(8L, l);
    }

    @Test //Le nombre d'épisodes de la saison 8 de la série Dexter
    public void Test027() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT (e) FROM Episode e JOIN e.saison e2 JOIN e2.serie e3 WHERE e3.titre='Dexter' AND e2.numSaison=8 ").getSingleResult();
        //alternative "SELECT COUNT (e) FROM Episode e WHERE e.saison.serie.titre='Dexter' AND e.saison.numSaison=8"
        Assert.assertEquals(12L, l);
    }

    @Test //Le nombre total d'épisodes de la série Dexter
    public void Test029() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long l = (long) em.createQuery("SELECT COUNT (e) FROM Episode e JOIN e.saison e2 JOIN e2.serie e3 WHERE e3.titre='Dexter'").getSingleResult();
        Assert.assertEquals(96L, l);
    }

    @Test //Le nombre total de liens pour nos films policiers américains
    public void Test031() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long z = (long) em.createQuery("SELECT COUNT (l) FROM Lien l JOIN l.film f JOIN f.genre g JOIN f.pays p WHERE g.nom='Policier' AND p.nom='USA' ").getSingleResult();
        Assert.assertEquals(3L, z);
    }

    @Test //Le nombre totals de liens pour nos films d'horreur interprétés par Polanski
    public void Test033() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        long z = (long) em.createQuery("SELECT COUNT (l) FROM Lien l JOIN l.film f JOIN f.genre g JOIN f.acteurs a WHERE g.nom='Horreur' AND a.nom='Polanski' ").getSingleResult();
        Assert.assertEquals(1L, z);
    }

    @Test
    public void Test035() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        List z = (List) em.createQuery("SELECT f FROM Film f WHERE f.genre.nom='Horreur' EXCEPT SELECT f FROM Film f WHERE f.acteurs.nom='Polanski'").getResultList();
        Assert.assertEquals("0L", z.size());

        //autre requete alterno
        String sol = "SELECT COUNT(f) FROM Film f JOIN f.genre g WHERE g.nom='Horreur' AND NOT EXIST(SELECT f.id FROM Film f JOIN f.acteur a WHERE a.nom='Polanski') ";

        /*alternative
          List<Long> films = em.createQuery("SELECT f.id FROM Film f WHERE f.genre.nom='Horreur' EXCEPT SELECT f.id FROM  Film f WHERE f.acteurs.nom='Polanski'").getResultList();
          Long[] idsAttendus = new Long[0];
          assertArrayEquals(idsAttendus,films.toArray());
         */
    }

    /*@Test //Tous les films d'horreur, sauf ceux interprétés par Polanski ( utiliser UNION ou EXCEPT  ou INTERSECT NOT IN, NOT EXIST)
    public void Test035() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        List z = (List) em.createQuery("SELECT DISTINCT f.titre FROM Film f JOIN f.acteurs a WHERE a.nom!='Polanski'").getResultList();
        Assert.assertEquals("Le bal des vampires, La Vénus à la fourrure, Big Lebowski (The), Fargo", z);
        
         Query q = em.createQuery("SELECT f.titre FROM Film f JOIN f.acteurs a WHERE a.nom!='Polanski'");
        Film f2 = (Film) q.getResultList();
        Assert.assertEquals("Fargo", f2);
         
    }*/
    @Test //pas safe
    public void Test037() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        List z = (List) em.createQuery("SELECT f FROM Film f INTERSECT SELECT f FROM Film f JOIN f.acteurs a WHERE a.nom='Polanski'").getSingleResult();
        Assert.assertEquals(9L, z);
    }

    /*@Test //Parmi tous les films, uniquement ceux interprétés par Polanski  ( utiliser UNION ou EXCEPT ou INTERSECT )
    public void Test037() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        List z = (List) em.createQuery("SELECT f.titre (f) FROM Film f JOIN f.acteurs a WHERE a.nom='Polanski'").getSingleResult();
         Assert.assertEquals(9L, z);
    }
     @Test //Tous les films interprétés par Polanski et aussi tous les films d'horreur ( utiliser UNION ou EXCEPT ou INTERSECT )
    public void Test039() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
    List z = (List) em.createQuery("SELECT DISTINCT f.titre FROM Film f JOIN f.acteurs a WHERE a.nom!='Polanski'").getResultList();
         Assert.assertEquals("Le bal des vampires, La Vénus à la fourrure, Big Lebowski (The), Fargo", z);
     */
    @Test //pas safe
    public void Test039() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        List z = (List) em.createQuery("SELECT f FROM Film f WHERE f.genre.nom='Horrreur' UNION SELECT f FROM Film f JOIN f.acteur a WHERE a.nom='Polanski'").getResultList();
        List<Long> films = em.createQuery("SELECT f.id FROM Film f WHERE f.genre.nom='Horreur' EXCEPT SELECT f.id FROM  Film f WHERE f.acteurs.nom='Polanski'").getResultList();
        Long[] idsAttendus = new Long[0];
        assertArrayEquals(idsAttendus, films.toArray());
    }
}
