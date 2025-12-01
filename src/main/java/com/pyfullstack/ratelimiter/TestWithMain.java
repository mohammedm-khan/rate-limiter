package com.pyfullstack.ratelimiter;
import java.util.ArrayList;
import java.util.List;

public class TestWithMain {
    public static void main(String[] args) {
        Solution s = new Solution();
        s.permute(new int[]{7, 8, 9});
    }
}


class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums);
        return result;
    }

    static int count = 0;

    private void backtrack(List<List<Integer>> result, List<Integer> tempList, int[] nums) {
        int call = ++count;
        if (tempList.size() == nums.length) {
            result.add(new ArrayList<>(tempList));
        } else {
            for (int i = 0; i < nums.length; i++) {
                if (tempList.contains(nums[i])) continue; // Skip if already used
                tempList.add(nums[i]);
                backtrack(result, tempList, nums);
                tempList.remove(tempList.size() - 1); // Backtrack
            }
        }
        System.out.println(String.format("call: %d, count: %d", call, count));
    }
}
