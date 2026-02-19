package com.artur114.srptowerdefense.common.pathfinding;

import java.util.Arrays;

public class PathBuildHeap {
    private static PathBuilder[] buildersHeap = new PathBuilder[128];
    private static int cursor = 0;

    private PathBuilder[] pathBuilders = new PathBuilder[128];
    private PathPointForced target;
    private int count;


    public void init(PathPointForced target) {
        this.target = target;
    }

    public void clearPath() {
        this.target = null;
        this.count = 0;
    }

    public PathBuilder addPath(PathBuilder path) {
        if (this.count >= this.pathBuilders.length) {
            this.pathBuilders = Arrays.copyOf(this.pathBuilders, this.pathBuilders.length * 2);
        }

        this.pathBuilders[this.count] = path;
        this.sortBack(this.count++);
        return path;
    }

    public PathBuilder dequeue() {
        PathBuilder path = this.pathBuilders[0];
        this.pathBuilders[0] = this.pathBuilders[--this.count];
        this.pathBuilders[this.count] = null;

        if (this.count > 0) {
            this.sortForward(0);
        }

        return path;
    }

    public void releaseAll() {
        for (int i = 0; i != this.count; i++) {
            this.releaseBuilder(this.pathBuilders[i]);
        }
    }

    public synchronized void releaseBuilder(PathBuilder builder) {
        if (builder == null) {
            return;
        }
        if (cursor >= buildersHeap.length) {
            buildersHeap = Arrays.copyOf(buildersHeap, buildersHeap.length * 2);
        }
        buildersHeap[cursor++] = builder.clear();
    }

    public synchronized PathBuilder createBuilder() {
        if (cursor != 0) {
            PathBuilder builder = buildersHeap[--cursor];
            buildersHeap[cursor] = null;

            if (builder == null) {
                builder = new PathBuilder();
            }

            return builder.setTarget(this.target);
        } else {
            return new PathBuilder().setTarget(this.target);
        }
    }

    public PathBuilder copy(PathBuilder parent) {
        PathBuilder builder = this.createBuilder();
        return builder.copyFrom(parent);
    }

    private void sortBack(int index) {
        PathBuilder path = this.pathBuilders[index];
        int i;

        for (float f = path.lpDistanceToTarget; index > 0; index = i) {
            i = index - 1 >> 1;
            PathBuilder path1 = this.pathBuilders[i];

            if (f >= path1.lpDistanceToTarget) {
                break;
            }

            this.pathBuilders[index] = path1;
        }

        this.pathBuilders[index] = path;
    }

    private void sortForward(int index) {
        PathBuilder path = this.pathBuilders[index];
        float f = path.lpDistanceToTarget;

        while (true)
        {
            int i = 1 + (index << 1);
            int j = i + 1;

            if (i >= this.count)
            {
                break;
            }

            PathBuilder path1 = this.pathBuilders[i];
            float f1 = path1.lpDistanceToTarget;
            PathBuilder path2;
            float f2;

            if (j >= this.count)
            {
                path2 = null;
                f2 = Float.POSITIVE_INFINITY;
            }
            else
            {
                path2 = this.pathBuilders[j];
                f2 = path2.lpDistanceToTarget;
            }

            if (f1 < f2)
            {
                if (f1 >= f)
                {
                    break;
                }

                this.pathBuilders[index] = path1;
                index = i;
            }
            else
            {
                if (f2 >= f)
                {
                    break;
                }

                this.pathBuilders[index] = path2;
                index = j;
            }
        }

        this.pathBuilders[index] = path;
    }

    public boolean isPathEmpty() {
        return this.count == 0;
    }
}