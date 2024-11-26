//package com.conquer_team.files_system;
//
//import com.github.difflib.DiffUtils;
//import com.github.difflib.patch.AbstractDelta;
//import com.github.difflib.patch.Patch;
//
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
//
//public class test {
//    public static void compareFiles() throws Exception {
//
//        List<String> oldFileLines = Files.readAllLines(Paths.get("src/main/resources/static/folders/2024-11-22_11_32_053d96bde1-afbc-4be6-8284-4ae4fc1df763_categories.pdf/test1.txt"));
//        List<String> newFileLines = Files.readAllLines(Paths.get("src/main/resources/static/folders/2024-11-22_11_32_053d96bde1-afbc-4be6-8284-4ae4fc1df763_categories.pdf/test2.txt"));
//
//        String newFileContent = String.join("\n", newFileLines);
//        int newFileSizeInBytes = newFileContent.getBytes("UTF-8").length;
//        Patch<String> patch = DiffUtils.diff(oldFileLines, newFileLines);
//
//        Set<Integer> changedNewLines = new TreeSet<>();
//        if (patch.getDeltas().isEmpty()) {
//            System.out.println("No differences found between the files.");
//        } else {
//            String st1 = null;
//            int size = 0;
//            int revisedEndLine = 0;
//            for (AbstractDelta<String> delta : patch.getDeltas()) {
//                st1 = delta.getType().toString();
//                size = newFileSizeInBytes;
//                String changeType = delta.getType().toString();
//                int revisedStartLine = delta.getTarget().getPosition() + 1;
//                revisedEndLine = revisedStartLine + delta.getTarget().getLines().size() - 1;
//                for (int i = revisedStartLine; i <= revisedEndLine; i++) {
//                    changedNewLines.add(i);
//                }
//                if (changeType.equals("CHANGE") || changeType.equals("INSERT") || changeType.equals("DELETE")) {
//                    for (int i = revisedStartLine; i <= revisedEndLine; i++) {
//
//                        System.out.println("Line: " + i + " | Change Type: " + changeType);
//
//                        if (changeType.equals("CHANGE") || changeType.equals("INSERT")) {
//                            System.out.println("New content: " + delta.getTarget().getLines().get(i - revisedStartLine));
//                        }
//
//                        if (changeType.equals("DELETE")) {
//                            System.out.println("Old content: " + delta.getSource().getLines().get(i - revisedStartLine));
//                        }
//                    }
//                }
//            }
//            // نوع التعديل الذي حصل على الملف
//            System.out.println(st1);
//            // حجم الملف المعدل
//            System.out.println(size);
//            // عدد الاسطر التي حدث فيها التعديل
//            System.out.println(changedNewLines.size());
//
//        }
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
