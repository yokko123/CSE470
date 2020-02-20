/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package runner;

/**
 *
 * @author sazid
 */
public class Runner {

    public static void main(String[] args) {
        input i = new input();
        int a = i.inp();
        
        output o = new output();
        o.out(a);
    }
    
}
