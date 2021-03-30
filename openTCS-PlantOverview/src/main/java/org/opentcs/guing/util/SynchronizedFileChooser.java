/*
 * openTCS copyright information:
 * Copyright (c) 2017 Fraunhofer IML
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.guing.util;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A {@link JFileChooser} which synchronizes the selected file with a correct file filter
 * and vice versa.
 * The file filters are only checked if they are of type {@link FileNameExtensionFilter}, because
 * with a normal {@link FileFilter} the value of a file extension is unknown.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public class SynchronizedFileChooser
    extends javax.swing.JFileChooser {

  /**
   * The selected file of this file chooser.
   */
  private String selectedFileName;

  /**
   * Creates a new instance.
   *
   * @param currentDirectory The current directory of the file chooser
   */
  public SynchronizedFileChooser(File currentDirectory) {
    super(currentDirectory);
    initComponents();
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      public void propertyChange(java.beans.PropertyChangeEvent evt) {
        formPropertyChange(evt);
      }
    });
  }// </editor-fold>//GEN-END:initComponents

  private void formPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_formPropertyChange
    //Synchronize if the user selects a different filter
    if (evt.getPropertyName().equals(FILE_FILTER_CHANGED_PROPERTY)) {
      if (selectedFileName == null
          || (getSelectedFile() != null && selectedFileName.equals(getSelectedFile().getName()))) {
        return;
      }
      File file = new File(getCurrentDirectory(), selectedFileName);
      //We currently have no file selected so we dont need to modify it
      //The equals check ensures that both events wont trigger each other over and over
      Object filter = evt.getNewValue();
      if (filter instanceof FileNameExtensionFilter) {
        FileNameExtensionFilter extensionFilter = (FileNameExtensionFilter) filter;
        int fileExtensionIndex = getFileExtensionIndex(file);
        //Select the first extension of the filter as the new file extension
        String newExtension = extensionFilter.getExtensions()[0];
        //If the file has a known file ending, replace it. Else just add the new extension
        //at the end
        StringBuilder newPathBuilder = new StringBuilder();
        newPathBuilder.append(fileExtensionIndex >= 0
            ? file.getName().substring(0, fileExtensionIndex) : file.getName());
        if (!newPathBuilder.toString().endsWith(".")) {
          newPathBuilder.append(".");
        }
        newPathBuilder.append(newExtension);
        //Update the chosen file
        setSelectedFile(new File(file.getParent(), newPathBuilder.toString()));
      }
    }
    //Synchronize if the user selects a different file
    else if (evt.getPropertyName().equals(SELECTED_FILE_CHANGED_PROPERTY)) {
      Object newFile = evt.getNewValue();
      if (newFile instanceof File) {
        this.selectedFileName = ((File) newFile).getName();
      }
    }
  }//GEN-LAST:event_formPropertyChange

  /**
   * Returns the index of the file extension in the file name if the current file ending is
   * known by one of the file name extension filters.
   *
   * @param file The file
   * @return The index of a known file extension in the file's name or <code>-1</code>
   */
  private int getFileExtensionIndex(File file) {
    for (FileFilter filter : getChoosableFileFilters()) {
      if (filter instanceof FileNameExtensionFilter) {
        FileNameExtensionFilter extensionFilter = (FileNameExtensionFilter) filter;
        for (String extension : extensionFilter.getExtensions()) {
          if (file.getName().endsWith(extension)) {
            return file.getName().length() - extension.length();
          }
        }
      }
    }
    return -1;
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
