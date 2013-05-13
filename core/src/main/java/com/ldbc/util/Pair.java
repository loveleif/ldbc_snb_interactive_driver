package com.ldbc.util;

public class Pair<T1, T2>
{
    private T1 thing1;
    private T2 thing2;

    public static <Type1, Type2> Pair<Type1, Type2> create( Type1 t1, Type2 t2 )
    {
        return new Pair<Type1, Type2>( t1, t2 );
    }

    public Pair( T1 t1, T2 t2 )
    {
        thing1 = t1;
        thing2 = t2;
    }

    public T1 _1()
    {
        return thing1;
    }

    public T2 _2()
    {
        return thing2;
    }

    @Override
    public String toString()
    {
        return "Pair [thing1=" + thing1 + ", thing2=" + thing2 + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( thing1 == null ) ? 0 : thing1.hashCode() );
        result = prime * result + ( ( thing2 == null ) ? 0 : thing2.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        Pair other = (Pair) obj;
        if ( thing1 == null )
        {
            if ( other.thing1 != null ) return false;
        }
        else if ( !thing1.equals( other.thing1 ) ) return false;
        if ( thing2 == null )
        {
            if ( other.thing2 != null ) return false;
        }
        else if ( !thing2.equals( other.thing2 ) ) return false;
        return true;
    }

}
