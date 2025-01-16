//package com.conquer_team.files_system;
//
//import com.github.difflib.DiffUtils;
//import com.github.difflib.patch.AbstractDelta;
//import com.github.difflib.patch.DeltaType;
//import com.github.difflib.patch.Patch;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
//
//public class test {
//    public static void compareFiles() throws Exception {
//        System.out.println("Ameen");
//        List<String> oldFileLines = Files.readAllLines(Paths.get("src/main/resources/static/folders/2025-01-14_12_06_2708236a60-205c-432c-8df8-0ee6738161c8_test1.txt/2025-01-14_12_15_39f83fd649-d2a8-4159-9071-ba0f13d68462_test1.txt"));
//        List<String> newFileLines = Files.readAllLines(Paths.get("src/main/resources/static/folders/2025-01-14_12_06_2708236a60-205c-432c-8df8-0ee6738161c8_test1.txt/2025-01-14_15_01_065e2f7a1c-c8a3-411b-9c8c-f1dfffa7dcb4_test1.txt"));
//        Patch<String> patch = DiffUtils.diff(oldFileLines, newFileLines);
//        Path path = Paths.get("src/main/resources/static/folders/2025-01-14_12_06_2708236a60-205c-432c-8df8-0ee6738161c8_test1.txt/2025-01-14_15_01_065e2f7a1c-c8a3-411b-9c8c-f1dfffa7dcb4_test1.txt");
//        double size = Files.size(path) ;
////        String newFileContent = String.join("\n", newFileLines);
////        double newFileSizeInBytes = newFileContent.getBytes("UTF-8").length;
//        System.out.println("size update = "+size+"bytes");
//        int totalline = 0;
//        for (AbstractDelta<String> delta : patch.getDeltas()) {
//            DeltaType type = delta.getType();
//            List<String> sourceLines = delta.getSource().getLines();
//            List<String> targetLines = delta.getTarget().getLines();
//
//            int sourcePosition = delta.getSource().getPosition() + 1; // +1 to make it 1-based index
//            int targetPosition = delta.getTarget().getPosition() + 1;
//
//            System.out.println("Change Type: " + type);
//
//            if (type == DeltaType.DELETE) {
//                System.out.println("Deleted Lines (starting from line " + sourcePosition + "): " + sourceLines);
//            } else if (type == DeltaType.INSERT) {
//                System.out.println("Added Lines (starting from line " + targetPosition + "): " + targetLines);
//            } else if (type == DeltaType.CHANGE) {
//                System.out.println("Original Lines (starting from line " + sourcePosition + "): " + sourceLines);
//                System.out.println("Modified To (starting from line " + targetPosition + "): " + targetLines);
//            }
//            totalline++;
//        }
//        System.out.println("totallineModified = "+totalline);
//    }
//
//    public static void main(String[] args) {
//        try {
//            compareFiles();
//        } catch (Exception e) {
//            e.getLocalizedMessage();
//        }
//    }
//}