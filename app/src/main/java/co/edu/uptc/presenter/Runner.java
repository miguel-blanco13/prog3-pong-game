package co.edu.uptc.presenter;

import co.edu.uptc.interfaces.ModelInterface;
import co.edu.uptc.interfaces.PresenterInterface;
import co.edu.uptc.interfaces.ViewInterface;
import co.edu.uptc.model.GameEngine;
import co.edu.uptc.view.GameFrame;

import javax.swing.*;

public class Runner {

    private PresenterInterface presenter;
    private ModelInterface     model;
    private ViewInterface      view;

    private void makeMVP() {
        presenter = new GamePresenter();
        model     = new GameEngine();
        view      = new GameFrame();
        presenter.setModel(model);
        presenter.setView(view);
        view.setPresenter(presenter);
    }

    public void start() {
        makeMVP();
        SwingUtilities.invokeLater(view::start);
    }
}
