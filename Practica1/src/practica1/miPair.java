/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica1;

/**
 *
 * @author oriolvidal
 * @param <fst>
 * @param <snd>
 */
public class miPair<fst, snd> {
    private fst first;
    private snd second;
    
    public miPair(fst first, snd second) {
        this.first = first;
        this.second = second;
    }
    
    public void setFirst(fst f) {
        this.first = f;
    }
    
    public void setSecond (snd s) {
        this.second = s;
    }
    public fst getFirst() {
        return this.first;
    }
    
    public snd getSecond() {
        return this.second;
    }
}
